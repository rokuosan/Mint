package com.deviseworks;

import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class ExecuteClass {
//    実行内容選択関数
    public String askExecuteContents(){

        Scanner scanner = new Scanner(System.in);

        String doContents;

        while(true) {
            System.out.print("\n> ");
            doContents = scanner.nextLine();
            for (ExecutableContentsEnum e : ExecutableContentsEnum.values()) {
                if (doContents.equalsIgnoreCase(e.name())) {
                    return doContents;
                }
            }
            for(ExecutableContentsEnum content: ExecutableContentsEnum.values()){
                System.out.println("\t" + content.ordinal() + ", " + content);
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
        for(SoftwareEnum e: SoftwareEnum.values()){
            System.out.println(e.ordinal() + ", " + e);
            System.out.println("\t" + e.getDescription());
        }
        System.out.print("\nどれを使用しますか？: ");

        String software;

        boolean flag = true;
        do {
            software = scanner.nextLine();
            for (SoftwareEnum s : SoftwareEnum.values()) {
                if (software.equalsIgnoreCase(s.toString()) || software.equalsIgnoreCase(String.valueOf(s.toString().charAt(0))) || software.equalsIgnoreCase(String.valueOf(s.ordinal()))) {
                    flag=false;
                    software = s.toString();
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
}
