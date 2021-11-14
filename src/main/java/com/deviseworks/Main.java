package com.deviseworks;

import com.deviseworks.actions.MainAction;
import com.deviseworks.actions.Settings;
import com.deviseworks.enumerate.SoftwareEnum;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Main {
    final static String VERSION = "v0.1.1";
    final static String CHANNEL = "dev";
    final static String BUILD = VERSION + "-" + CHANNEL;

    public static void main(String[] args) {

        if (args.length == 0) {
            MainAction action = new MainAction();
            Settings settings = new Settings();
            settings.init();

            // 起動時メッセージ
            System.out.println("Instant Instance " + BUILD + "\n");
            action.dialog();

        } else {
//            System.out.println("コマンドライン引数には現在対応していません。\nのちのち実装予定です");
            enum State{
                NONE,
                SOFTWARE,
                BUILD,
                VERSION,
                DIRECTORY
            }

            State state = State.NONE;
            String software=null;
            String version=null;
            String build=null;
            boolean isAutoApprove = false;
            String directory=null;

            for (String arg : args) {
                if(arg.startsWith("-")){
                    if(state == State.NONE){
                        switch (arg.substring(1)) {
                            case "h" -> {
                                System.out.println("Arguments:");
                                System.out.println("\t-h : Show Helps");
                                System.out.println("\t-s : Select server software");
                                System.out.println("\t-v : Select a version");
                                System.out.println("\t-b : Select a build");
                                System.out.println("\t-y : Auto Approve (Not Recommended)");
                                System.out.println("\t-d : Select the directory to be used during installation. (Enclose in \"\")");
                                return;
                            }
                            case "s" -> {
                                if(arg.equalsIgnoreCase(args[args.length-1])){
                                    System.out.println("\t- 引数が足りません: " + arg);
                                    return;
                                }
                                state = State.SOFTWARE;
                            }
                            case "v" -> {
                                if(arg.equalsIgnoreCase(args[args.length-1])){
                                    System.out.println("\t- 引数が足りません: " + arg);
                                    return;
                                }
                                state = State.VERSION;
                            }
                            case "b" -> {
                                if(arg.equalsIgnoreCase(args[args.length-1])){
                                    System.out.println("\t- 引数が足りません: " + arg);
                                    return;
                                }
                                state = State.BUILD;
                            }
                            case "y" -> isAutoApprove = true;
                            case "d" -> {
                                if(arg.equalsIgnoreCase(args[args.length-1])){
                                    System.out.println("\t- 引数が足りません: " + arg);
                                    return;
                                }
                                state = State.DIRECTORY;
                            }
                            default -> {
                                System.out.println("\t- 無効な引数");
                                return;
                            }
                        }
                    }else{
                        System.out.println("\t- 構文エラー");
                    }
                }else{
                    switch(state){
                        case NONE -> System.out.println("\t- 無効な引数");
                        case SOFTWARE -> software = arg;
                        case VERSION -> version = arg;
                        case BUILD -> build = arg;
                        case DIRECTORY -> directory = arg;
                    }
                    state = State.NONE;
                }
            }
            if(software != null){
                boolean isHit = false;
                for(SoftwareEnum e: SoftwareEnum.values()){
                    if(software.equalsIgnoreCase(e.toString())){
                        isHit = true;
                        break;
                    }
                }
                if(!isHit){
                    System.out.println("\t- 無効なソフトウェア名 : " + software);
                    return;
                }
                software = software.toLowerCase();

                switch(software){
                    case "paper" -> {
                        // バージョン取得
                        JSONObject json = new PaperActionsClass().getVersion(false);
                        if(Objects.isNull(json)){return;}
                        JSONArray items = json.getJSONArray("versions");
                        if(version != null){
                            isHit = false;
                            for(int i=0; i<items.length(); i++){
                                if(version.equalsIgnoreCase(String.valueOf(items.get(i)))){
                                    isHit=true;
                                }
                            }
                            if(!isHit){
                                System.out.println("\t- 無効なバージョン : " + version);
                                return;
                            }
                        }else{
                            System.out.println("\t- 最新バージョンを使用します");
                            version = String.valueOf(items.get(items.length()-1));
                        }
                        // ビルド取得
                        json = new PaperActionsClass().getBuilds(version, false);
                        if(Objects.isNull(json)){return;}
                        items = json.getJSONArray("builds");
                        if(build != null){
                            isHit = false;
                            for(int i=0;i<items.length();i++){
                                if(build.equalsIgnoreCase(String.valueOf(items.get(i)))){
                                    isHit=true;
                                }
                            }
                            if(!isHit){
                                System.out.println("\t- 無効なビルド番号 : " + build);
                                return;
                            }
                        }else{
                            System.out.println("\t- 最新ビルドを使用します");
                            build = String.valueOf(items.get(items.length()-1));
                        }
                        // ディレクトリ
                        Path path;
                        if(directory != null){
                            try{
                                path = Paths.get(directory);
                            }catch(InvalidPathException e){
                                System.out.println("\t- 無効なディレクトリパス");
                                return;
                            }
                        }else{
                            path = Paths.get("").toAbsolutePath();
                            int tag = 100;
                            while(true) {
                                if(!Files.exists(Paths.get(path + "/Paper/" + version + "/" + tag + "/"))) {
                                    path = Paths.get(path + "/Paper/" + version + "/" + tag + "/");
                                    break;
                                }else{
                                    tag++;
                                }
                            }
                        }
                        // 確認
                        String full_url = "https://papermc.io/api/v2/projects/paper/versions/" + version + "/builds/" + build + "/downloads/paper-" + version + "-" + build + ".jar";

                        if(!new MediaUtilitiesClass().confirm(isAutoApprove, software, version, build, String.valueOf(path), full_url)){
                            return;
                        }

                        // ディレクトリ作成
                        new MediaUtilitiesClass().check(path, full_url);
                        String downloadName =  "paper-" + version + "-" + build + ".jar";
                        new MediaUtilitiesClass().createScript(path, downloadName);
                    }
                    case "mohist" -> {
                        // バージョン
                        if(version != null){
                            String[] versions = {
                                    "1.12.2",
                                    "1.16.5"
                            };
                            isHit=false;
                            for(String v: versions){
                                if(v.equalsIgnoreCase(version)){
                                    isHit = true;
                                    break;
                                }
                            }
                            if(!isHit){
                                System.out.println("\t- 無効なバージョン : " + version);
                                return;
                            }
                        }else{
                            version = "1.16.5";
                        }
                        // URL
                        String full_url = "https://mohistmc.com/api/" + version + "/latest";
                        JSONObject json = new MediaUtilitiesClass().getJSONObject(full_url);
                        build = String.valueOf(json.get("number"));
                        if(build != null){
                            System.out.println("\t- 指定されたビルドを最新のビルドに上書きします(" + build + ")");
                        }
                        String downloadLink = "https://mohistmc.com/builds/" + version + "/" + json.getString("name");
                        // ディレクトリ
                        Path path;
                        if(directory != null){
                            try{
                                path = Paths.get(directory);
                            }catch(InvalidPathException e){
                                System.out.println("\t- 無効なディレクトリパス");
                                return;
                            }
                        }else{
                            path = Paths.get("").toAbsolutePath();
                            int tag = 100;
                            while(true) {
                                if(!Files.exists(Paths.get(path + "/Mohist/" + version + "/" + tag + "/"))) {
                                    path = Paths.get(path + "/Mohist/" + version + "/" + tag + "/");
                                    break;
                                }else{
                                    tag++;
                                }
                            }
                        }
                        // 確認
                        if(new MediaUtilitiesClass().confirm(isAutoApprove, software, version, build, String.valueOf(path), downloadLink)) {
                            new MediaUtilitiesClass().check(path, full_url);
                            String downloadName = json.getString("name");
                            new MediaUtilitiesClass().createScript(path, downloadName);
                        }
                    }
                    case "cuberite" -> new CuberiteActionsClass().download(isAutoApprove, directory);
                }
            }
        }
    }
}