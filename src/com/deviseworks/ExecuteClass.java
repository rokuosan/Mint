package com.deviseworks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

// JColor (装飾)
import com.diogonunes.jcolor.AnsiFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.diogonunes.jcolor.Attribute.RED_TEXT;
import static com.diogonunes.jcolor.Ansi.colorize;

public class ExecuteClass {
//    実行内容選択関数
    public String askExecuteContents(){

        Scanner scanner = new Scanner(System.in);

        String doContents;

        while(true) {
            System.out.print("\nなにを実行しますか？ (helpと入力して一覧を表示):");
            doContents = scanner.nextLine();
            for (ExecutableContentsEnum e : ExecutableContentsEnum.values()) {
                if (doContents.equalsIgnoreCase(e.name())) {
                    return doContents;
                }
            }
        }
    }

//    コマンド実行関数
    public void executeCommand(String command){

        switch (command) {
            case "install" -> {
                String software = selectSoftware();
                String version = selectVersion(software);
                String build = selectBuild(software, version);
                downloadServer(software, version, build);
            }
            case "uninstall" -> System.out.println("現在制作中です");
            case "setting" -> System.out.println("現在制作中です。");
        }
    }


//    ソフトウェア選択関数
    public String selectSoftware(){
        Scanner scanner = new Scanner(System.in); // Create Scanner Class
        System.out.println();
        for(ServerSoftwareEnum software: ServerSoftwareEnum.values()){ // 画面表示
            System.out.print(software.ordinal() + ", ");
            System.out.println(software);
        }
        System.out.print("\nどれを使用しますか？: ");

        String software;

        boolean flag = true;
        do {
            software = scanner.nextLine();
            for (ServerSoftwareEnum s : ServerSoftwareEnum.values()) {
                if (software.equalsIgnoreCase(s.toString()) || software.equalsIgnoreCase(String.valueOf(s.toString().charAt(0))) || software.equalsIgnoreCase(String.valueOf(s.ordinal()))) {
                    flag=false;
                    software = s.name();
                }
            }
            if(flag) {
                System.out.print("識別できませんでした。\nもう一度入力してください: ");
            }
        }while(flag);

        return software;
    }

//    バージョン選択関数
    public String selectVersion(String software) {
        Scanner scanner = new Scanner(System.in);
        PaperActionsClass paperActions = new PaperActionsClass();
        JSONObject json;
        JSONArray items;

        // 取得したバージョンリストを表示するメソッドを後々追加してね。

        // PAPER の場合（他のソフトウェアと同じかわからないから今はIFで分岐してる）
        if (software.equalsIgnoreCase("paper")) {
            json = paperActions.getVersion(false);
            String tempVersion;
            while (true) {
                System.out.print("\n使用するバージョンを入力 (listで一覧表示): ");
                tempVersion = scanner.nextLine();
                items = json.getJSONArray("versions");
                if (tempVersion.equalsIgnoreCase("list")) {
                    System.out.println("[=]");
                    for (int i = 0; i < items.length(); i++) {
                        System.out.print(" | ");
                        System.out.println(items.get(i));
                    }
                }
                if(tempVersion.equalsIgnoreCase("latest")){
                    System.out.println("\n\t- 最新バージョンを使用します");
                    String temp="";
                    for(int i=0; i<items.length(); i++){
                        temp = (String) items.get(i);
                    }
                    return temp;
                }
                for (int i = 0; i < items.length(); i++) {
                    if (tempVersion.equalsIgnoreCase((String) items.get(i))) {
                        return tempVersion;
                    }
                }
            }
        }

        return "ERROR";
    }

//    ビルド選択
    public String selectBuild(String software, String version){
        PaperActionsClass paperAction = new PaperActionsClass(); // Paperクラス作成
        Scanner scanner = new Scanner(System.in);
        JSONObject json;
        JSONArray items;

        String tempBuild;

       if(software.equalsIgnoreCase("paper")){
           // 初期設定
           json = paperAction.getBuilds(version, false);
           items = json.getJSONArray("builds");

           // 実行内容
           while(true) {
               System.out.print("\n使用するビルドを入力(listで一覧表示): ");
               tempBuild = scanner.nextLine();
               if(tempBuild.equalsIgnoreCase("list")){
                   System.out.println("[=]");
                   for(int i=0;i<items.length();i++){
                       System.out.print(" | ");
                       System.out.println(items.get(i));
                   }
               }
               if(tempBuild.equalsIgnoreCase("latest")){
                   System.out.println("\n\t- 最新ビルドを使用します");
                   String temp="";
                   for(int i=0; i<items.length(); i++){
                       temp = String.valueOf(items.get(i));
                   }
                   return temp;
               }
               for(int i=0; i<items.length(); i++){
                   if (tempBuild.equalsIgnoreCase(String.valueOf(items.get(i)))) {
                       return tempBuild;
                   }
               }
           }
       }

        return software + version;
    }

//    サーバーソフトウェアダウンロード
    public void downloadServer(String software, String version, String build) {
        PaperActionsClass paperAction = new PaperActionsClass(); // Paperクラス作成
        if (software.equalsIgnoreCase("paper")) {
            paperAction.download(version, build);
        }
    }


//    ファイル・フォルダ作成関数
    public void createFiles(){
        // 文字装飾フォーマット
        AnsiFormat WarningF = new AnsiFormat(RED_TEXT());

        // サポートバージョンリストオブジェクト作成
        Path path_supportedVersions = Paths.get("supportedVersions"); // パス
        File file_supportedVersions = new File("supportedVersions"); // ファイル

        // ファイルが存在するか確認
        if(!Files.exists(path_supportedVersions)){
            System.out.println(colorize("supportedVersions hasn't been found.", WarningF));
            try {
                Files.createFile(path_supportedVersions);
                System.out.println(" -> generated supportedVersions");
                try {
                    FileWriter fileWriter = new FileWriter(file_supportedVersions); //書き込みオブジェクト

                    fileWriter.write("1.7.10\r\n1.8.9\r\n1.12.2\r\n1.14.4\r\n1.16.2\r\n1.17.1");

                    fileWriter.close(); // 終了
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(colorize("入出力エラーが発生しました", WarningF));
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(colorize("ファイル作成時にエラーが発生しました", WarningF));
            }
        }
    }
}
