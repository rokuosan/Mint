package com.deviseworks;

import java.util.Objects;
import java.util.Scanner;

import org.json.JSONObject;
import org.json.JSONArray;

public class ExecuteClass {
//    実行内容選択関数
    public String askExecuteContents(){

        Scanner scanner = new Scanner(System.in);

        String doContents;

        while(true) {
            System.out.print("> ");
            doContents = scanner.nextLine();
            // 利用可能なコマンドか列挙型を走査する
            for (ExecutableContentsEnum e : ExecutableContentsEnum.values()) {
                if (doContents.equalsIgnoreCase(e.name())) {
                    return doContents;
                }
            }
            // コマンドではない場合、ヘルプを表示
            for(ExecutableContentsEnum content: ExecutableContentsEnum.values()){
                System.out.println("\t" + content.ordinal() + ", " + content);
            }
        }
    }

//    コマンド実行関数
    public void executeCommand(String command){
        switch (command) {
            case "install" -> {
                String software = selectSoftware(); //ソフトウェアの選択
                String version = selectVersion(software); //バージョンの選択
                if("ERROR".equalsIgnoreCase(version)){
                    return;
                }
                String build = selectBuild(software, version); //ビルド番号の選択
                downloadServer(software, version, build); // サーバーをダウンロード
            }
            case "uninstall" -> System.out.println("現在制作中です");
            case "setting" -> {
                String option;
                boolean flag = true;
                do {
                    SettingClass setting = new SettingClass();
                    option = setting.selectSetting(flag);
                    System.out.println();
                    setting.parseSetting(option);
                    System.out.println();
                    flag = false;
                }while(!option.equalsIgnoreCase("exit"));
            }
        }
    }


//    ソフトウェア選択関数
    public String selectSoftware(){
        Scanner scanner = new Scanner(System.in);
        System.out.println(); // 空改行
        for(SoftwareEnum e: SoftwareEnum.values()){
            System.out.println(e.ordinal() + ", " + e);
            System.out.println("\t" + e.getDescription());
        }

        String software;
        System.out.print("使用するソフトウェアを選択してください");
        while(true){
            System.out.print("\nSELECT_SOFTWARE> ");
            software = scanner.nextLine();
            // 入力した文字列がソフトウェアと一致するか列挙型を走査する
            for(SoftwareEnum e: SoftwareEnum.values()){
                // 大小問わず一致・頭文字一致・番号一致の場合実行
                if(software.equalsIgnoreCase(e.toString()) || software.equalsIgnoreCase(String.valueOf(e.toString().charAt(0))) || software.equalsIgnoreCase(String.valueOf(e.ordinal()))){
                    return e.toString();
                }
            }
        }
    }

//    バージョン選択関数
    public String selectVersion(String software) {
        Scanner scanner = new Scanner(System.in);
        PaperActionsClass paperActions = new PaperActionsClass();
        CuberiteActionsClass cuberiteActions = new CuberiteActionsClass();
        JSONObject json;
        JSONArray items;

        // PAPER の場合（他のソフトウェアと同じかわからないから今はIFで分岐してる）
        if (software.equalsIgnoreCase("paper")) {
            json = paperActions.getVersion(false);
            if(Objects.isNull(json)){
                return "ERROR";
            }
            String tempVersion;
            System.out.print("使用するバージョンを入力してください (listで一覧表示)");
            while (true) {
                System.out.print("\nSELECT_VERSION> ");
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
        }else if(software.equalsIgnoreCase("cuberite")){
            cuberiteActions.download();
        }

        return software;
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
           System.out.print("使用するビルドを入力してください (listで一覧表示)");
           while(true) {
               System.out.print("\nSELECT_BUILD> ");
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
