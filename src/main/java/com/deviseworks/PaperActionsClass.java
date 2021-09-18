package com.deviseworks;

import java.io.*;
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

import org.json.JSONObject;

import static java.lang.System.out;

public class PaperActionsClass {
    // バージョンを取得して一覧表示する
    public JSONObject getVersion(boolean isAsync){
        // HttpClient の作成
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(20)).build();
        // HttpRequest の作成
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://papermc.io/api/v2/projects/paper/")).build();
        // HTTPResponse の作成
        HttpResponse<String> response;
        // リクエストの送信
        if(isAsync) {
        // 非同期処理(未完成)
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> out.println(res.body()));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null; //非同期に対応したときにこの行を削除
        }else{
        // 同期処理
            out.print("\n\t- バージョンを取得しています...");
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                out.println("[完了]\n");
                return new JSONObject(Objects.requireNonNull(response).body());
            } catch (IOException e) {
                out.println("[失敗]");
                out.println("\t- リクエストに失敗しました。インターネットに接続されているか確認してください。\n");
                return null;
            } catch (InterruptedException e) {
                return null;
            }
        }
    }

//    ビルド取得
    public JSONObject getBuilds(String version, boolean isAsync){
        //完全なURI作成
        String full_uri = "https://papermc.io/api/v2/projects/paper/versions/" + version;

        // HttpClient の作成
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        // HttpRequest の作成
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(full_uri)).build();
        // HTTPResponse の作成
        HttpResponse<String> response = null;

        if(isAsync){ // 非同期は未完成なので使わない
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> out.println(res.body()));
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }else{
            out.print("\t- ビルドを取得しています...");
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                out.println("[完了]\n");
            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
                out.println("[失敗]");
                out.println("\n\t- リクエストに失敗しました。インターネットに接続されているか確認してください。\n");
            }
        }

        return new  JSONObject(Objects.requireNonNull(response).body());
    }

//    jar ダウンロード
    public void download(String version, String build){
        // Scanner クラス作成
        Scanner scanner = new Scanner(System.in);
        MediaUtilitiesClass util = new MediaUtilitiesClass();

        // 完全なURI作成
        String full_uri = "https://papermc.io/api/v2/projects/paper/versions/" + version + "/builds/" + build + "/downloads/paper-" + version + "-" + build + ".jar";

        // 生成したリンクの確認とダウンロードの承認
        out.print("\t- 以下のリンクからソフトウェアをダウンロードします。\n\t- ");
        out.println(full_uri);
        String confirm; // 承認用変数
        while(true){
            out.print("\nよろしいですか？(yes/no): ");
            confirm = scanner.nextLine();
            if(!confirm.isBlank()){
                if(confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")){
                    break;
                }else{
                    out.println("キャンセルしました\n");
                    return;
                }
            }
        }
        // Path
        Path current = Paths.get("").toAbsolutePath(); //カレントディレクトリ取得

        // ファイルがすでに存在するか確認する
        int tag = 100;
        Path check;
        while(true) {
            check = Paths.get(current + "/paper/" + version + "/" + tag + "/");
            if(!Files.exists(check)) {
                try {
                    Files.createDirectories(check);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }else{
                tag++;
            }
        }

        // 取得
        URL url;
        try {
            url = new URL(full_uri);
            out.print("\n\t- ダウンロード中です...");
            if(util.downloadFile(url, check)){
                out.println("[完了]");
            }else {
                out.println("失敗");
                return;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // 起動用バッチファイル作成
        if(util.isWindows()){
            out.print("\t- テスト用起動バッチファイルを作成中...");
            try {
                Files.createFile(Paths.get(check + "/start.bat"));
                FileWriter fileWriter = new FileWriter(check + "/start.bat"); //書き込みオブジェクト
                fileWriter.write("@echo off\r\njava -Xmx" + SettingClass.getMaxMemory() + "G -Xms" + SettingClass.getMinMemory() + "G -server -jar paper-" + version + "-" + build + ".jar nogui\r\npause");
                fileWriter.close(); // 終了
                out.println("[完了]");
                out.println("\t- サーバーを起動するには " + check + "\\start.bat を起動してください");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(util.isLinux()){
            out.print("\t- テスト起動用スクリプトを作成中...");
            try {
                Files.createFile(Paths.get(check + "/start.sh"));
                FileWriter fw = new FileWriter(check + "/start.sh");
                fw.write("#!/bin/bash\r\njava -Xmx" + SettingClass.getMaxMemory() + "G -Xms" + SettingClass.getMinMemory() + "G -server -jar paper-" + version + "-" + build + ".jar nogui");
                out.println("[完了]");
                out.println("\t- サーバーを起動するには " + check + "/start.sh を起動してください");
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            out.println("\t- 起動用スクリプトは作成されませんでした");
        }
    }
}