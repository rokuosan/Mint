package com.deviseworks.actions;

import com.deviseworks.util.Internet;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URL;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Mohist {
    private final String baseApi = "https://mohistmc.com/api/";

    private final Internet internet = new Internet();

    // Function: getVersions
    public List<String> getVersions(){
        // APIのページをスクレイピングでバージョンを取得
        try {
            // リスト宣言
            List<String> versions = new ArrayList<>();
            // 接続
            Document doc = Jsoup.connect(baseApi).get();
            // 要素取得
            Elements versionElements = doc.select("ul.wpb_tabs_nav span");
            for(Element versionElement: versionElements){
                versions.add(versionElement.ownText());
            }

            return versions;
        }catch(Exception e){
            return null;
        }
    }

    // Function: getBuilds
    // Argument: version
    public List<String> getBuilds(String version){
        List<String> builds = new ArrayList<>(); // ビルドリスト
        JSONObject master; // JSONオブジェクト
        String api = baseApi + version; // API

        HttpResponse<String> response = internet.connectResponse(api); // APIを叩く
        if(response == null){ // Nullチェック
            return null;
        }

        // 元のJSONデータ
        master = new JSONObject(response.body());
        // 中のJSONデータ
        JSONObject obj;

        // 走査する
        int i=1;
        while(true){
            try{
                obj = master.getJSONObject(String.valueOf(i));
                // 成功ビルドのみ追加する
                if(obj.getString("status").equalsIgnoreCase("SUCCESS")){
                    builds.add(obj.get("number").toString());
                }
            }catch(Exception e){
                break;
            }
            i++;
        }

        return builds;
    }

    // Function: download
    // Argument: Path, version, build
    public boolean download(Path path, String version, String build){
        try{
            String api = baseApi + version;
            // APIをしばく
            HttpResponse<String> response = internet.connectResponse(api);
            JSONObject object = new JSONObject(response.body());
            // ビルド取得
            object = object.getJSONObject(build);
            // ダウンロードリンクを取得
            String link = object.getString("url");

            URL url = new URL(link);
            return internet.downloadFile(url, path);
        }catch(Exception e){
            return false;
        }
    }

    // Function: install
    // Argument: path,  version, build, force
    public boolean install(Path path, String version, String build, boolean force){
        String ver = null;
        String bld = null;

        if(!force){
            // バージョン取得
            List<String> versions = this.getVersions();
            for(String v: versions){
                if(v.equalsIgnoreCase(version)){
                    ver = v;
                }
            }

            // ビルド取得
            List<String> builds = this.getBuilds(ver);
            if(builds != null){
                for(String b: builds){
                    if(b.equalsIgnoreCase(build)){
                        bld = b;
                    }
                }
            }else{
                return false;
            }
        }else{
            ver = version;
            bld = build;
        }

        // ダウンロード
        return download(path, ver, bld);
    }

    public boolean install(Path path){
        List<String> versions = this.getVersions(); // バージョン取得
        String version = versions.get(versions.size()-1); // バージョンを最新版に
        List<String> builds = this.getBuilds(version); // ビルド取得
        String build = builds.get(builds.size()-1); // ビルドを最新版に

        return this.install(path, version, build, true);
    }
}
