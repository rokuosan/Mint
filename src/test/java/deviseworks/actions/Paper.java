package deviseworks.actions;

import deviseworks.util.Internet;
import org.json.JSONArray;
import org.json.JSONObject;

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
        } catch (Exception e) {
            return false;
        }
    }

    // Function: install(Path)
    // Description: Install Paper to your computer. Use the latest version and build.
    public boolean install(Path path){
        List<String> versions = getVersions(); // バージョン取得
        String version = versions.get(versions.size()-1); // バージョンを最新版に
        List<String> builds = getBuilds(version); // ビルド取得
        String build = builds.get(builds.size()-1); // ビルドを最新版に

        return install(path, version, build, true);
    }

    // Function: install(Path, Version, Build, Force)
    // Description: Install Paper to your computer. Use the argument's version and build.
    //              If put true on the 4th argument, install it without verification.
    public boolean install(Path path, String version, String build, boolean force){
        String ver = null; // バージョン
        String bld = null; // ビルド

        if(!force){
            // バージョンを設定
            List<String> versions = getVersions(); // バージョンをAPIから取得
            for (String v : versions) {    // 取得したバージョンリストから引数のバージョンを探す
                if (version.equalsIgnoreCase(v)) {
                    ver = v; // 見つかった場合、バージョンを確定する
                }
            }
            if (ver == null) { // 見つからない場合(引数が間違っている場合)はfalseを返す
                return false;
            }

            // ビルドを確定する
            List<String> builds = getBuilds(ver); // ビルドをAPIから取得
            for (String b : builds) { // ビルドを探す
                if (b.equalsIgnoreCase(build)) {
                    bld = b; // 一致した場合、確定する
                }
            }
            if (bld == null) { // 見つからない場合falseを返す
                return false;
            }
        }else{
            ver = version;
            bld = build;
        }

        // ダウンロード
        return download(path, ver, bld);
    }
}
