package com.deviseworks;

import java.net.MalformedURLException;
import java.net.URL;
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
        try {
            if (arch.equalsIgnoreCase("amd64")) {
                System.out.println("[64bit]");
                download_url = new URL("https://download.cuberite.org/windows-x86_64/Cuberite.zip");
            }else if(arch.equalsIgnoreCase("x86")){
                System.out.println("[32bit]");
                download_url = new URL("https://download.cuberite.org/windows-i386/Cuberite.zip");
            }else{
                System.out.println("[UNKNOWN]");
                System.out.println("\t- アーキテクチャ取得に失敗");
                return;
            }
            System.out.println("\t- ダウンロードリンクを更新しました");
        }catch(MalformedURLException e){
            System.out.println("[失敗]\n\t- アーキテクチャ取得に失敗");
            return;
        }

        // ダウンロード先を指定
        Path current = Paths.get("").toAbsolutePath();
        Path full_path = Paths.get(current + "/Cuberite/");

        int tag = 100;

        System.out.print("\t- インストールディレクトリをサーチ中...");
        while(true){
            full_path = Paths.get(full_path + "/" + tag);
            if(util.checkDirectory(full_path)){
                break;
            }else{
                tag++;
            }
        }

        // ダウンロード
        System.out.print("\n以下のリンクからダウンロードを行います\n\t- " + download_url + "\n\nよろしいですか？(Y/n): ");
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

    }
}
