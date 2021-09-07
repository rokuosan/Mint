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
import static com.diogonunes.jcolor.Attribute.RED_TEXT;
import static com.diogonunes.jcolor.Ansi.colorize;

public class ExecuteClass {
//    実行内容選択関数
    public String askExecuteContents(){

        Scanner scanner = new Scanner(System.in);

        System.out.print("なにを実行しますか？ (helpと入力して一覧を表示):");
        String doContents;

        while(true) {
            doContents = scanner.nextLine();
            for (ExecutableContentsEnum e : ExecutableContentsEnum.values()) {
                if (doContents.equalsIgnoreCase(e.name())) {
                    if(doContents.equalsIgnoreCase("help")){
                        System.out.println("** Command List / コマンドリスト **");
                        System.out.println("1, install - インスタンスを作成する");
                        System.out.println("2, uninstall - インスタンスを削除する");
                        System.out.println("3, setting - 詳細設定を行う");
                        System.out.println("4, help - このリストを表示");
                    }else {
                        return doContents;
                    }
                }
            }
            System.out.print("\nなにを実行しますか？ (helpと入力して一覧を表示):");
        }
    }

//    コマンド実行関数
    public void executeCommand(String command){

        switch(command){
            case "install":
                String software = selectSoftware();
                selectVersion(software);
            case "uninstall":

            case "setting":
        }
    }


//    ソフトウェア選択関数
    public String selectSoftware(){
        Scanner scanner = new Scanner(System.in); // Create Scanner Class

        for(ServerSoftwareEnum software: ServerSoftwareEnum.values()){ // 画面表示
            System.out.print(software.ordinal() + ", ");
            System.out.println(software);
        }
        System.out.print("どれを使用しますか？: ");

        String software;

        boolean flag = true;
        do {
            software = scanner.nextLine();
            for (ServerSoftwareEnum s : ServerSoftwareEnum.values()) {
                if (software.equalsIgnoreCase(s.toString())) {
                    flag=false;
                }else if(software.equalsIgnoreCase(String.valueOf(s.ordinal()))){
                    flag=false;
                    software = s.name();
                }
                if(software.equalsIgnoreCase(String.valueOf(s.toString().charAt(0)))){
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
    public int selectVersion(String software){
        Scanner scanner = new Scanner(System.in);
        PaperActionsClass paperActions = new PaperActionsClass();

        int version=0;

        System.out.println(software + " のバージョンを選択します");
        // 取得したバージョンリストを表示するメソッドを後々追加してね。
        if(software.equalsIgnoreCase("paper")){
            paperActions.getVersion(false);
        }

        String tempVersion;
        System.out.print("バージョンを入力: ");
        while(true) {
            tempVersion = scanner.nextLine();
            if (!tempVersion.equalsIgnoreCase("")) {
                break;
            }
            System.out.println("認識できませんでした。もう一度入力してください: ");
        }

        return version;
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
