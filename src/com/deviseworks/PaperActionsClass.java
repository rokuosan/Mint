package com.deviseworks;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;

public class PaperActionsClass {
//    TODO
//     https://hacknote.jp/archives/31234/
//     https://java-code.jp/1557
//     上のようなサイトを参考に
//     https://papermc.io/api/v2/projects/paper/versions/1.17.1/
//     こういうところから取得したJSONを解析して一覧表示する

    // バージョンを取得して一覧表示する
    public JSONObject getVersion(){
        JSONObject json = new JSONObject(this.getVersion(false));
        return json;
    }

    public JSONObject getVersion(boolean isAsync){
        // HttpClient の作成
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        // HttpRequest の作成
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://papermc.io/api/v2/projects/paper/")).build();
        // HTTPResponse の作成
        HttpResponse<String> response = null;
        // リクエストの送信
        if(isAsync) {
        // 非同期処理
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(res -> System.out.println(res.body()));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
        // 同期処理
            System.out.print("バージョンを取得しています...");
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
//                e.printStackTrace();
                System.out.println("[失敗]");
                System.out.println("\nリクエストに失敗しました。インターネットに接続されているか確認してください。\n");
            }
            System.out.println("[完了]");
//            System.out.println(Objects.requireNonNull(response).body()); // デバッグ
        }
        // JSON オブジェクト作成
        JSONObject json = new JSONObject(response.body());
        // JSON Array 作成
//        JSONArray items = json.getJSONArray("versions");

        return json;
    }
}

///api/v2/projects/paper/versions/1.17.1/builds
//
//        https://papermc.io/api/v2/projects/paper/versions/1.17.1/builds/250/downloads/paper-1.17.1-250.jar
//
//        {
//        "project_id":"paper",
//        "project_name":"Paper",
//        "version":"1.17.1",
//        "builds":
//        [
//        81,83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,99,100,102,103,104,105,106,107,108,109,110,111,112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,192,193,194,195,196,197,198,199,200,201,202,204,205,206,207,208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,240,241,242,243,244,245,246,247,249,250,251
//        ]
//        }
//
//https://maven.minecraftforge.net/net/minecraftforge/forge/1.17.1-37.0.53/forge-1.17.1-37.0.53-installer.jar