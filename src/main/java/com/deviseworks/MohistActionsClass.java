package com.deviseworks;

import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.System.out;

public class MohistActionsClass {
    // ビルド選ばせるのは面倒だからバージョンのみ聞いてあとは最新ビルドを問答無用で使用する
    public boolean install(){
        var versions = new String[]{
               "1.12.2",
               "1.16.5"
        };

        for(var v: versions){
            System.out.println("\t- " + v);
        }

        System.out.println("\n使用するバージョンを選択してください");
        Scanner scanner = new Scanner(System.in);
        var flag = true;
        String version;
        do{
            System.out.print("SELECT VERSIONS> ");
            version = scanner.nextLine();
            for(String v: versions){
                if(v.equalsIgnoreCase(version)){
                    flag = false;
                    version = v;
                }
            }
        }while(flag);

        /*
         * HTTP関連
         */
        // URL作成
        String url_str = "https://mohistmc.com/api/" + version + "/latest";

        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(20)).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url_str)).build();
        HttpResponse<String> response;

        JSONObject json;
        System.out.print("\t- ビルドを取得中です...");
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            json = new JSONObject(Objects.requireNonNull(response).body());
            System.out.println("[完了]");
        }catch (Exception e){
            System.out.println("[失敗]");
            return false;
        }
//        String build = String.valueOf(json.getJSONObject("number"));
        String build = json.getString("name");

        System.out.println("\t- 最新のビルドを使用します (" + build + ")");

//        String download_name = String.valueOf(json.getJSONObject("name"));
        String download_name = json.getString("name");
        String download_link = "https://mohistmc.com/builds/" + version + "/" + download_name;

        System.out.println("\t- 以下のリンクからダウンロードします\n\t- " + download_link + "\n");

        System.out.println("よろしいですか？(yes/no)");
        while(true){
            System.out.print("CONFIRM> ");
            String confirm = scanner.nextLine();
            if("yes".equalsIgnoreCase(confirm) || "y".equalsIgnoreCase(confirm)){
                break;
            }else if("no".equalsIgnoreCase(confirm) || "n".equalsIgnoreCase(confirm)){
                System.out.println("キャンセルしました");
                return false;
            }
        }

        // Path
        Path current = Paths.get("").toAbsolutePath();

        // ファイルがすでに存在するか確認する
        System.out.println("\t- ディレクトリを作成しています。");
        int tag = 100;
        Path check;
        while(true) {
            check = Paths.get(current + "/Mohist/" + version + "/" + tag + "/");
            if(!Files.exists(check)) {
                try {
                    Files.createDirectories(check);
                    System.out.println("\t- ディレクトリを以下の場所に作成しました\n\t- " + check);
                } catch (IOException e) {
                    System.out.println("\t- ディレクトリの作成に失敗しました");
                }
                break;
            }else{
                tag++;
            }
        }

        MediaUtilitiesClass util = new MediaUtilitiesClass();

        URL url;
        try {
            url = new URL(download_link);
            System.out.print("\t- ダウンロード中です...");
            if(util.downloadFile(url, check)){
                System.out.println("[完了]\n");
            }else {
                System.out.println("[失敗]\n");
                return false;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(util.isWindows()){
            out.print("\t- テスト用起動バッチファイルを作成中...");
            try {
                Files.createFile(Paths.get(check + "/start-test.bat"));
                FileWriter fileWriter = new FileWriter(check + "/start-test.bat"); //書き込みオブジェクト
                fileWriter.write("@echo off\r\njava -Xmx" + SettingClass.getMaxMemory() + "G -Xms" + SettingClass.getMinMemory() + "G -server -jar " + download_name + " nogui\r\npause");
                fileWriter.close(); // 終了
                out.println("[完了]");
                out.println("\t- サーバーを起動するには " + check + "\\start-test.bat を起動してください");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(util.isLinux()){
            out.print("\t- テスト起動用スクリプトを作成中...");
            try {
                Files.createFile(Paths.get(check + "/start-test.sh"));
                FileWriter fw = new FileWriter(check + "/start-test.sh");
                fw.write("#!/bin/bash\njava -Xmx" + SettingClass.getMaxMemory() + "G -Xms" + SettingClass.getMinMemory() + "G -server -jar " + download_name + " nogui\n");
                fw.close();
                out.println("[完了]");
                out.println("\t- サーバーを起動するには " + check + "/start-test.sh を起動してください");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            out.println("\t- 起動用スクリプトは作成されませんでした");
        }

        return true;
    }
}
