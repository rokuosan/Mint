package com.deviseworks.actions;

import java.io.FileOutputStream;
import java.util.Properties;

public class Settings {
    public void init(){
        Properties settings = new Properties();
        settings.setProperty("maxMemory", "4");
        settings.setProperty("minMemory", "4");

        try (FileOutputStream out = new FileOutputStream("settings.properties")) {
            settings.store(out, "Settings Properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}