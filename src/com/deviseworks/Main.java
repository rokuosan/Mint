package com.deviseworks;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


import java.util.InputMismatchException;
import java.util.Scanner;

// JColor (装飾)
import com.diogonunes.jcolor.AnsiFormat;
import static com.diogonunes.jcolor.Ansi.colorize;
import static com.diogonunes.jcolor.Attribute.RED_TEXT;


public class Main {

    public static void main(String[] args) {
        // 文字装飾フォーマット
//        AnsiFormat WarningF = new AnsiFormat(RED_TEXT());

        // サポートバージョンリストオブジェクト作成
//        Path path_supportedVersions = Paths.get("supportedVersions"); // パス
//        File file_supportedVersions = new File("supportedVersions"); // ファイル

        // ファイルが存在するか確認
//        if(!Files.exists(path_supportedVersions)){
//            System.out.println(colorize("supportedVersions hasn't been found.", WarningF));
//            try {
//                Files.createFile(path_supportedVersions);
//                System.out.println(" -> generated supportedVersions");
//                try {
//                    FileWriter fileWriter = new FileWriter(file_supportedVersions); //書き込みオブジェクト
//
//                    fileWriter.write("1.7.10\r\n1.8.9\r\n1.12.2\r\n1.14.4\r\n1.16.2\r\n1.17.1");
//
//                    fileWriter.close(); // 終了
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    System.out.println(colorize("入出力エラーが発生しました", WarningF));
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                System.out.println(colorize("ファイル作成時にエラーが発生しました", WarningF));
//            }
//        }

        // 起動時メッセージ
        System.out.println("########## Thank you for using this software. ##########");
        System.out.println("# This software is under development.                  #");
        System.out.println("# 現在開発中です。                                        #");
        System.out.println("########################################################");

        // 実行内容を選択
        AskExecuteClass execute = new AskExecuteClass();
        execute.askExecuteContents();


        // サーバーソフトウェアの選択
        Scanner scanner = new Scanner(System.in); // スキャナオブジェクト作成

        for(ServerSoftwareEnum software: ServerSoftwareEnum.values()){ // 画面表示
            System.out.print(software.ordinal() + ", ");
            System.out.println(software);
        }
        System.out.print("What will you use?: ");
        String software;
        boolean flag = true;
        do {
            software = scanner.nextLine();
            for (ServerSoftwareEnum s : ServerSoftwareEnum.values()) {
                if (software.equalsIgnoreCase(s.toString())) {
                    flag=false;
                }else if(software.equalsIgnoreCase(String.valueOf(s.ordinal()))){
                    flag=false;
                }

                switch (software) {
                    case "v", "f", "p" -> flag = false;
                    default -> {
                    }
                }
            }
            if(flag) {
                System.out.print("\nTry again: ");
            }
        }while(flag);

        // Minecraftバージョンの選択


    }
}