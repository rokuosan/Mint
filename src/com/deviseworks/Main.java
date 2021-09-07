package com.deviseworks;

public class Main {

    public static void main(String[] args) {

        // 起動時メッセージ
        System.out.println("########## Thank you for using this software. ##########");
        System.out.println("# This software is under development.                  #");
        System.out.println("# 現在開発中です。                                        #");
        System.out.println("########################################################");

        // 実行内容を選択
        PaperActionsClass paperActions = new PaperActionsClass();
        paperActions.getVersion(false);
//        ExecuteClass execute = new ExecuteClass();
//        String command =  execute.askExecuteContents();
//
//        execute.executeCommand(command);

    }
}