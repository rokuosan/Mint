package com.deviseworks;

import java.util.Scanner;

public class ExecuteClass {
    public void askExecuteContents(){

        Scanner scanner = new Scanner(System.in);

        // 実行内容を尋ねる
        System.out.print("What do you want to do?(Type help to show help):");
        boolean flag=true;
        String doContents;

        while(flag) {
            doContents = scanner.nextLine();
            for (ExecutableContentsEnum e : ExecutableContentsEnum.values()) {
                if (doContents.equalsIgnoreCase(e.name())) {
                    if(doContents.equalsIgnoreCase("help")){
                        System.out.println("** Command List **");
                        System.out.println("1, install - Install Instance of MC Server.");
                        System.out.println("2, uninstall - Uninstall instance.");
                        System.out.println("3, setting - Edit Some actions.");
                        System.out.println("4, help - Show this list.");
                    }else {
                        flag = false;
                        break;
                    }
                }
            }
            if(flag) {
                System.out.print("Try again.(Type help to show help): ");
            }
        }
    }
}
