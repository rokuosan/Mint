package deviseworks;

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

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MediaUtilitiesClass {
//    public void downloadFile(URL url) throws IOException {
//        String path = url.getPath();
//        String name = path.substring(path.lastIndexOf("/") +1);
//        Files.copy(url.openStream(), Paths.get(name), REPLACE_EXISTING);
//    }

    public boolean downloadFile(URL url, Path path){
        String urlPath = url.getPath();
        String name = urlPath.substring(urlPath.lastIndexOf("/") +1);
        try {
            Files.copy(url.openStream(), Paths.get(path + "/" + name), REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean checkDirectory(Path path){
        return !Files.exists(path);
    }

    public boolean createDirectory(Path path){
        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isWindows(){
        final String OS_NAME = System.getProperty("os.name").toLowerCase();
        return OS_NAME.startsWith("windows");
    }
    public boolean isLinux(){
        final String OS_NAME = System.getProperty("os.name").toLowerCase();
        return OS_NAME.startsWith("linux");
    }
    public JSONObject getJSONObject(String url){
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofSeconds(20)).build();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
        HttpResponse<String> response;

        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new JSONObject(Objects.requireNonNull(response).body());
        }catch (Exception e){
            return null;
        }
    }

    public void createScript(Path path, String downloadName){
        if(new MediaUtilitiesClass().isWindows()){
            System.out.print("\t- テスト用起動バッチファイルを作成中...");
            try{
                Files.createFile(Paths.get(path + "/start-test.bat"));
                FileWriter writer = new FileWriter(path + "/start-test.bat");
                writer.write("@echo off\r\njava -Xmx" + SettingClass.getMaxMemory() + "G -Xms" + SettingClass.getMinMemory() + "G -server -jar " + downloadName + " nogui\r\npause");
                writer.close();
                System.out.println("[完了]");
                System.out.println("\t- サーバーを起動するには " + path + "\\start-test.bat を起動してください");
            }catch(IOException e){
                System.out.println("[失敗]");
            }
        }else if(new MediaUtilitiesClass().isLinux()){
            System.out.print("\t- テスト起動用スクリプトを作成中...");
            try {
                Files.createFile(Paths.get(path + "/start-test.sh"));
                FileWriter fw = new FileWriter(path + "/start-test.sh");
                fw.write("#!/bin/bash\njava -Xmx" + SettingClass.getMaxMemory() + "G -Xms" + SettingClass.getMinMemory() + "G -server -jar " + downloadName + " nogui\n");
                fw.close();
                System.out.println("[完了]");
                System.out.println("\t- サーバーを起動するには " + path + "/start-test.sh を起動してください");
            }catch(IOException e){
                System.out.println("[失敗]");
            }
        }else{
            System.out.println("\t- 起動用スクリプトは作成されませんでした");
        }
    }
    public boolean confirm(boolean isAutoApprove, String software, String version, String build, String path, String full_url){
        System.out.println("\n以下の構成でインストールします:");
        System.out.println("\t- ソフトウェア: " + software);
        System.out.println("\t- バージョン: " + version);
        System.out.println("\t- ビルド番号: " + build);
        System.out.println("\t- インストールパス: " + path);
        System.out.println("\t- ダウンロードリンク: " + full_url);
        if(!isAutoApprove){
            System.out.print("\nよろしいですか?(Y/n): ");
            String confirm = new Scanner(System.in).nextLine();
            if (!(confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y"))) {
                System.out.println("\t- キャンセルしました\n");
                return false;
            }
        }
        return true;
    }
    public boolean check(Path path, String full_url){
        if(!Files.exists(path)){
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                System.out.println("\t- ディレクトリ作成に失敗");
            }
        }
        // ダウンロード
        try {
            URL url = new URL(full_url);
            System.out.print("\t- ダウンロード中...");
            if(new MediaUtilitiesClass().downloadFile(url, path)){
                System.out.println("[完了]");
            }else{
                System.out.println("[失敗]");
                return false;
            }
        } catch (MalformedURLException e) {
            System.out.println("\t- ダウンロードリンク生成に失敗");
            return false;
        }
        return true;
    }
}
