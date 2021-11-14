package com.deviseworks;

import com.deviseworks.actions.MainAction;
import com.deviseworks.actions.Settings;

public class Main {
    final static String VERSION = "v0.1.1";
    final static String CHANNEL = "dev";
    final static String BUILD = VERSION + "-" + CHANNEL;

    public static void main(String[] args) {
        Settings settings = new Settings();
        settings.init();

        MainAction action = new MainAction();
        if (args.length == 0) {
            // 起動時メッセージ
            System.out.println("Instant Instance " + BUILD + "\n");
            action.dialog();

        } else {
            if(action.exec(args)==0){
                System.out.println("正常に終了しました");
            }
        }
    }
}