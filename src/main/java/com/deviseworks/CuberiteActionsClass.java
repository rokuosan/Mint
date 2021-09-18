package com.deviseworks;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class CuberiteActionsClass {
    public void download(){
        MediaUtilitiesClass util = new MediaUtilitiesClass();
        Scanner scanner = new Scanner(System.in);

        // ダウンロードURLの指定
        URL download_url;
        System.out.print("\t- アーキテクチャを取得中...");
        final String arch = System.getProperty("os.arch").toLowerCase();

        if(util.isWindows()) {
            try {
                if (arch.equalsIgnoreCase("amd64")) {
                    System.out.println("[64bit]");
                    download_url = new URL("https://download.cuberite.org/windows-x86_64/Cuberite.zip");
                } else if (arch.equalsIgnoreCase("x86")) {
                    System.out.println("[32bit]");
                    download_url = new URL("https://download.cuberite.org/windows-i386/Cuberite.zip");
                } else {
                    System.out.println("[UNKNOWN]");
                    System.out.println("\t- アーキテクチャ取得に失敗");
                    return;
                }
                System.out.println("\t- ダウンロードリンクを更新しました");
            } catch (MalformedURLException e) {
                System.out.println("[失敗]\n\t- アーキテクチャ取得に失敗");
                return;
            }
        }else if(util.isLinux()){
            try {
                if (arch.equalsIgnoreCase("amd64")) {
                    System.out.println("[64bit]");
                    download_url = new URL("https://download.cuberite.org/linux-x86_64/Cuberite.tar.gz");
                } else if (arch.equalsIgnoreCase("x86")) {
                    System.out.println("[32bit]");
                    download_url = new URL("https://download.cuberite.org/linux-i386/Cuberite.tar.gz");
                } else {
                    System.out.println("[UNKNOWN]");
                    System.out.println("\t- アーキテクチャ取得に失敗");
                    return;
                }
                System.out.println("\t- ダウンロードリンクを更新しました");
            } catch (MalformedURLException e) {
                System.out.println("[失敗]\n\t- アーキテクチャ取得に失敗");
                return;
            }
        }else{
            System.out.println("[失敗]");
            System.out.println("\t- Windows/Linuxのみサポートしています");
            return;
        }

        // ダウンロード先を指定
        Path current = Paths.get("").toAbsolutePath();
        Path full_path;

        int tag = 100;

        System.out.print("\t- インストールディレクトリをサーチ中...");
        while(true){
            full_path = Paths.get(current + "/Cuberite/" + tag);
            if(util.checkDirectory(full_path)){
                break;
            }else{
                tag++;
            }
        }

        // ダウンロード
        System.out.print("\n\t- 以下のリンクからダウンロードを行います\n\t- " + download_url + "\n\nよろしいですか？(Y/n): ");
        String confirm = scanner.nextLine();
        if(!(confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y"))){
            System.out.println("\t- キャンセルしました");
            return;
        }

        // ディレクトリ作成
        if(util.createDirectory(full_path)){
            System.out.println("\t- ディレクトリを作成");
            System.out.println("\t- " + full_path);
        }else{
            System.out.println("\t- ディレクトリの作成に失敗しました");
            return;
        }

        // DL開始
        System.out.print("\t- ダウンロードしています...");
        if(util.downloadFile(download_url, full_path)){
            System.out.println("[完了]");
        }else{
            System.out.println("[失敗]");
        }

        // 解凍
        // URLからファイル名を取得
        String filename = download_url.getPath().substring(download_url.getPath().lastIndexOf("/") +1);
        String filepath = full_path + "/" + filename;
        boolean isSuccess = false;
        if(util.isWindows()) {
            System.out.print("\t- ファイルを解凍中...");
            try {
                new ZipFile(filepath).extractAll(full_path.toString());
                System.out.println("[完了]");
                isSuccess = true;
            } catch (ZipException e) {
                System.out.println("[失敗]");
                e.printStackTrace();
            }
            // 解凍後の圧縮データを削除する
            if (isSuccess) {
                System.out.print("\t- 圧縮データを削除中...");
                try {
                    Files.delete(Paths.get(filepath));
                    System.out.println("[完了]");
                } catch (IOException e) {
                    System.out.println("[失敗]");
                }
            }
        }else if(util.isLinux()){
            System.out.println("\t- ファイルを解凍します...");
            try {
                Runtime runtime = Runtime.getRuntime();
                Process result = runtime.exec("tar -zxvf " + filepath + " -C " + full_path + "/");
                BufferedReader br = new BufferedReader(new InputStreamReader(result.getInputStream()));
                while (true) {
                    String line = br.readLine();
                    if (line == null) {
                        break;
                    }
                    System.out.println("\t- " + line);
                }
                isSuccess = true;
                System.out.println("\t- 解凍が完了しました");
            }catch(Exception e){
                System.out.println("\t- 解凍に失敗しました");
            }
            if(isSuccess){
                System.out.print("\t- 圧縮ファイルを削除します...");
                try {
                    Files.delete(Paths.get(filepath));
                    System.out.println("[完了]");
                } catch (IOException e) {
                    System.out.println("[失敗]");
                }
            }
        }
    }
}
