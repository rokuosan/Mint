package com.deviseworks;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CuberiteActionsClass {
    public URL setDownloadLink(){
        MediaUtilitiesClass util = new MediaUtilitiesClass();
        final String arch = System.getProperty("os.arch").toLowerCase();
        System.out.print("\t- アーキテクチャを取得中...");

        if(util.isWindows()) {
            try {
                if (arch.equalsIgnoreCase("amd64")) {
                    System.out.println("[Windows-64bit]");
                    return new URL("https://download.cuberite.org/windows-x86_64/Cuberite.zip");
                } else if (arch.equalsIgnoreCase("x86")) {
                    System.out.println("[Windows-32bit]");
                    return new URL("https://download.cuberite.org/windows-i386/Cuberite.zip");
                }else{
                    System.out.println("[UNKNOWN]");
                    System.out.println("\t- アーキテクチャ取得に失敗");
                    return null;
                }
            } catch (MalformedURLException e) {
                System.out.println("[失敗]\n\t- アーキテクチャ取得に失敗");
                return null;
            }
        }else if(util.isLinux()){
            try {
                if (arch.equalsIgnoreCase("amd64")) {
                    System.out.println("[Linux-64bit]");
                    return new URL("https://download.cuberite.org/linux-x86_64/Cuberite.tar.gz");
                } else if (arch.equalsIgnoreCase("x86")) {
                    System.out.println("[Linux-32bit]");
                    return new URL("https://download.cuberite.org/linux-i386/Cuberite.tar.gz");
                } else {
                    System.out.println("[UNKNOWN]");
                    System.out.println("\t- アーキテクチャ取得に失敗");
                    return null;
                }
            } catch (MalformedURLException e) {
                System.out.println("[失敗]\n\t- アーキテクチャ取得に失敗");
                return null;
            }
        }else{
            System.out.println("[失敗]");
            System.out.println("\t- Windows/Linuxのみサポートしています");
        }
        return null;
    }

    public void download(){
        this.download(false, null);
    }
    public void download(boolean isAutoApprove, String directory){
        MediaUtilitiesClass util = new MediaUtilitiesClass();

        // ダウンロードURLの指定
        URL download_url = setDownloadLink();
        if(download_url == null){
            return;
        }

        // ダウンロード先を指定
        Path path;
        if(directory != null){
            try{
                path = Paths.get(directory);
            }catch(InvalidPathException e){
                System.out.println("\t- 無効なディレクトリパス");
                return;
            }
        }else{
            path = Paths.get("").toAbsolutePath();
            int tag = 100;
            while(true) {
                if(!Files.exists(Paths.get(path + "/Cuberite/"+ tag + "/"))) {
                    path = Paths.get(path + "/Cuberite/" + tag + "/");
                    break;
                }else{
                    tag++;
                }
            }
        }

        // ダウンロード
        if(!(util.confirm(isAutoApprove, "cuberite", "latest", "latest", String.valueOf(path), String.valueOf(download_url)))){
            return;
        }

        // ディレクトリ作成
        util.check(path, String.valueOf(download_url));

        // 解凍
        // URLからファイル名を取得
        String filename = download_url.getPath().substring(download_url.getPath().lastIndexOf("/") +1);
        String filepath = path + "/" + filename;
        boolean isSuccess = false;
        if(util.isWindows()) {
            System.out.print("\t- ファイルを解凍中...");
            try {
                new ZipFile(filepath).extractAll(path.toString());
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
                Process result = runtime.exec("tar -zxvf " + filepath + " -C " + path + "/");
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
