package com.deviseworks.actions;

import com.deviseworks.util.Directory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class Settings {
    private final Directory directory = new Directory();

    public void init(){
        // ファイルがない場合
        if (directory.check(Paths.get(Paths.get("").toAbsolutePath()+"/settings.properties")) == 0) {
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

    public Properties getSettings(){
        Properties settings = new Properties();
        FileInputStream in;
        try{
            in = new FileInputStream("settings.properties");
            settings.load(in);
            in.close();
        }catch (Exception e){
            return null;
        }
        return settings;
    }

    public boolean setMaxMemory(int max){
        Properties settings = this.getSettings();
        settings.setProperty("maxMemory", String.valueOf(max));
        try (FileOutputStream out = new FileOutputStream("settings.properties")) {
            settings.store(out, "Settings Properties");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean setMinMemory(int min){
        if(min > this.getMaxMemory()){
            return false;
        }else{
            Properties settings = this.getSettings();
            settings.setProperty("minMemory", String.valueOf(min));
            try (FileOutputStream out = new FileOutputStream("settings.properties")) {
                settings.store(out, "Settings Properties");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }

    public int getMaxMemory(){
        Properties settings = this.getSettings();
        return Integer.parseInt(settings.getProperty("maxMemory"));
    }

    public int getMinMemory(){
        Properties settings = this.getSettings();
        return Integer.parseInt(settings.getProperty("minMemory"));
    }

}