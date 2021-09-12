package com.deviseworks;

import java.util.InputMismatchException;
import java.util.Scanner;

public class SettingClass {

    // 設定項目選択関数
    public String selectSetting(boolean ShowHelp){
        Scanner scanner = new Scanner(System.in);
        String command;
        if(ShowHelp) {
            System.out.println();
            for (SettingCommandsEnum e : SettingCommandsEnum.values()) {
                System.out.println(e.ordinal() + ", " + e + "\n\t" + e.getDescription());
            }
        }
        System.out.println("変更する設定名を入力してください");
        while(true){
            System.out.print("> ");
            command = scanner.nextLine();
            for(SettingCommandsEnum e: SettingCommandsEnum.values()){
                if(command.equalsIgnoreCase(e.toString())){
                    return e.toString();
                }
            }
        }
    }

    // 選択された設定項目から適切なメソッドを呼び出す関数
    public void parseSetting(String command){
        for(SettingCommandsEnum e: SettingCommandsEnum.values()){
            if(command.equalsIgnoreCase(e.toString())){
                switch(command){
                    case "setMaxMemory" -> {
                        int temp = askNumber();
                        setMaxMemory(temp);
                        System.out.println("最大メモリ容量が"+ temp + "GBに変更されました");
                    }
                    case "setMinMemory" -> {
                        int temp = askNumber();
                        if(temp > getMaxMemory()){
                            System.out.println("最大メモリ量を超えています");
                            return;
                        }
                        setMinMemory(temp);
                        System.out.println("最小メモリ容量が"+ temp + "GBに変更されました");
                    }
                    case "getMaxMemory" -> System.out.println("現在の最大メモリ量は" + getMaxMemory() + "GBです");
                    case "getMinMemory" -> System.out.println("現在の最小メモリ量は" + getMinMemory() + "GBです");
                    case "exit" -> System.out.println("設定を終了します。\n");
                }
            }
        }
    }

    // 数字を聞いて取り込む関数
    public int askNumber(){
        Scanner scanner = new Scanner(System.in);
        String temp;
        int number;

        System.out.println("数字を入力してください");
        while(true){
            System.out.print("> ");
            temp = scanner.nextLine();
            try{
                number = Integer.parseInt(temp);
            }catch (NumberFormatException e){
                continue;
            }
            return number;
        }
    }

    // テスト起動用メモリ量
    private static int MaxMemory = 4;
    private static int MinMemory = 4;

    public static void setMaxMemory(int memory){
        MaxMemory = memory;
    }
    public static int getMaxMemory(){
        return MaxMemory;
    }
    public static void setMinMemory(int memory){
        MinMemory = memory;
    }
    public static int getMinMemory(){
        return MinMemory;
    }
}
