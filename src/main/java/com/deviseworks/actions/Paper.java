package com.deviseworks.actions;

import com.deviseworks.util.Internet;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Paper {
    private final String api = "https://papermc.io/api/v2/projects/paper/";

    Internet util = new Internet(); // API叩くためのクラス
    JSONObject json;
    JSONArray items;

    // Function: getVersions
    // Description: Get Supported Versions from Paper API.
    public List<String> getVersions(){
        List<String> versions = new ArrayList<>(); // バージョンを格納する

        HttpResponse<String> response = util.connectResponse(api); // APIを叩く

        // nullチェック
        if(response == null){
            return null;
        }

        json = new JSONObject(response.body());     // レスポンスをJSONに変換
        items = json.getJSONArray("versions"); // JSONから配列を取得

        // それぞれパースして格納
        for(int i=0; i<items.length(); i++){
            versions.add(String.valueOf(items.get(i)));
        }

        return versions;
    }

    // Function: getBuilds
    // Description: Get supported builds from Paper API.
    public List<String> getBuilds(String version){
        List<String> builds = new ArrayList<>();

        HttpResponse<String> response = util.connectResponse(api+"/versions/"+version);

        if(response == null){
            return null;
        }

        json = new JSONObject(response.body());
        items = json.getJSONArray("builds");

        for(int i=0;i<items.length();i++){
            builds.add(String.valueOf(items.get(i)));
        }

        return builds;
    }

    // Function: download
    // Description: Download software.
    public boolean download(Path path, String version, String build){
        try {
            URL url = new URL(api + "versions/" + version + "/builds/" + build + "/downloads/paper-" + version + "-" + build + ".jar");
            return util.downloadFile(url, path);
        } catch (MalformedURLException e) {
            return false;
        }
    }

    // Function: install
    // Description: Install Paper to your computer.
    public boolean install(Path path){
        List<String> versions = getVersions(); // バージョン取得
        String version = versions.get(versions.size()-1); // バージョンを最新版に
        List<String> builds = getBuilds(version); // ビルド取得
        String build = builds.get(builds.size()-1); // ビルドを最新版に

        return download(path, version, build);
    }
}
