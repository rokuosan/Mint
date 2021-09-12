package com.deviseworks;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Scanner;

import org.json.JSONObject;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class PaperActionsClass {
//    TODO
//     https://hacknote.jp/archives/31234/
//     https://java-code.jp/1557
//     上のようなサイトを参考に
//     https://papermc.io/api/v2/projects/paper/versions/1.17.1/
//     こういうところから取得したJSONを解析して一覧表示する

    // バージョンを取得して一覧表示する
    public JSONObject getVersion(boolean isAsync){
        // HttpClient の作成
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        // HttpRequest の作成
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://papermc.io/api/v2/projects/paper/")).build();
        // HTTPResponse の作成
        HttpResponse<String> response = null;
        // リクエストの送信
        if(isAsync) {
        // 非同期処理(未完成)
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> System.out.println(res.body()));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
        // 同期処理
            System.out.print("\t- バージョンを取得しています...");
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
                System.out.println("[失敗]");
                System.out.println("\n\t- リクエストに失敗しました。インターネットに接続されているか確認してください。\n");
            }
            System.out.println("[完了]");
        }

        return new JSONObject(Objects.requireNonNull(response).body());
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
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> System.out.println(res.body()));
            try{
                Thread.sleep(3000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }else{
            System.out.print("\t- ビルドを取得しています...");
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
                System.out.println("[完了]");
            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
                System.out.println("[失敗]");
                System.out.println("\n\t- リクエストに失敗しました。インターネットに接続されているか確認してください。\n");
            }
        }

        return new  JSONObject(Objects.requireNonNull(response).body());
    }

//    jar ダウンロード
    public void download(String version, String build){
        // Scanner クラス作成
        Scanner scanner = new Scanner(System.in);

        // 完全なURI作成
        String full_uri = "https://papermc.io/api/v2/projects/paper/versions/" + version + "/builds/" + build + "/downloads/paper-" + version + "-" + build + ".jar";

        // 生成したリンクの確認とダウンロードの承認
        System.out.print("\t- 以下のリンクからソフトウェアをダウンロードします。\n\t- ");
        System.out.println(full_uri);
        String confirm; // 承認用変数
        while(true){
            System.out.println("\nよろしいですか？(yes/no): ");
            confirm = scanner.nextLine();
            if(!confirm.isBlank()){
                if(confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")){
                    break;
                }else{
                    System.out.println("キャンセルしました\n");
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
        try {
            System.out.print("\n\t- ダウンロード中です...");
            URL url = new URL(full_uri);
            Path path = Paths.get(check + "/paper-" + version + "-" + build + ".jar");
            Files.copy(url.openStream(), path, REPLACE_EXISTING);
            System.out.println("[完了]");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 起動用バッチファイル作成
        try {
            System.out.print("\t- テスト用起動バッチファイルを作成中...");
            Files.createFile(Paths.get(check + "/start.bat"));
            FileWriter fileWriter = new FileWriter(check + "/start.bat"); //書き込みオブジェクト
            fileWriter.write("@echo off\r\njava -Xmx" + SettingClass.getMaxMemory() + "G -Xms" + SettingClass.getMinMemory() + "G -server -jar paper-" + version + "-" + build + ".jar nogui\r\npause");
            fileWriter.close(); // 終了
            System.out.println("[完了]");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}

//https://papermc.io/api/v2/projects/paper/versions/1.17.1/builds/250/downloads/paper-1.17.1-250.jar
//https://maven.minecraftforge.net/net/minecraftforge/forge/1.17.1-37.0.53/forge-1.17.1-37.0.53-installer.jar