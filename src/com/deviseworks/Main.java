package com.deviseworks;

public class Main {

    public static void main(String[] args) {

        // 起動時メッセージ
        System.out.println("########## Thank you for using this software. ##########");
        System.out.println("# This software is under development.                  #");
        System.out.println("# 現在開発中です。                                        #");
        System.out.println("########################################################");
        System.out.println("\n** Command List / コマンドリスト **");
        System.out.println("1, install - インスタンスを作成する");
        System.out.println("2, uninstall - インスタンスを削除する");
        System.out.println("3, setting - 詳細設定を行う");
        System.out.println("4, help - このリストを表示\n");

        // 実行内容を選択
        ExecuteClass execute = new ExecuteClass();
        String command =  execute.askExecuteContents();

        execute.executeCommand(command);

    }
}