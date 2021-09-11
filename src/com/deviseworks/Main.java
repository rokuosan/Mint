package com.deviseworks;

public class Main {

    public static void main(String[] args) {

        ExecuteClass execute;
        String command;

        // 実行内容を選択
        while(true) {
            execute = new ExecuteClass();
            command = execute.askExecuteContents(); // 実行するコマンドを取り込む

            // 取り込んだコマンドを解析
            switch(command){
                case "exit" -> {
                    System.out.println("システムを終了します");
                    return;
                }
                case "help" -> {
                    for(ExecutableContentsEnum content: ExecutableContentsEnum.values()){
                        System.out.println("\t" + content.ordinal() + ", " + content);
                    }
                }
                default -> execute.executeCommand(command);
            }
        }
    }
}