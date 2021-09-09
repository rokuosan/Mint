package com.deviseworks;

public class Main {

    public static void main(String[] args) {

        // 起動時メッセージ
        System.out.println("########## Thank you for using this software. ##########");
        System.out.println("# This software is under development.                  #");
        System.out.println("# 現在開発中です。                                        #");
        System.out.println("########################################################");

        ExecuteClass execute;
        String command;

        // 実行内容を選択
        while(true) {
            execute = new ExecuteClass();
            command = execute.askExecuteContents();
            if(command.equalsIgnoreCase("exit")){
                System.out.println("システムを終了します");
                return;
            }else if(command.equalsIgnoreCase("help")){
                for(ExecutableContentsEnum content: ExecutableContentsEnum.values()){
                    System.out.print("\t");
                    System.out.print(content.ordinal());
                    System.out.print(", " + content.name()+ "\n");
                }
            }else{
                break;
            }
        }
        execute.executeCommand(command);

    }
}