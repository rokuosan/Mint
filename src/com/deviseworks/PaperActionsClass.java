package com.deviseworks;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

public class PaperActionsClass {
//    TODO
//     https://hacknote.jp/archives/31234/
//     https://java-code.jp/1557
//     上のようなサイトを参考に
//     https://papermc.io/api/v2/projects/paper/versions/1.17.1/
//     こういうところから取得したJSONを解析して一覧表示する

    // バージョンを取得して一覧表示する
    public void getVersion(boolean isAsync){
        // HttpClient の作成
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).build();
        // HttpRequest の作成
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://papermc.io/api/v2/projects/paper/")).build();
        // リクエストの送信
        if(isAsync) {
        // 非同期処理
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString()).thenAccept(response -> System.out.println(response.body()));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }else{
        // 同期処理
            HttpResponse<String> response = null;
            try {
                response = client.send(request, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Objects.requireNonNull(response).body());
        }
    }
}
