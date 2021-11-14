package com.deviseworks.actions;

import com.deviseworks.util.Directory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class Settings {
    private final Directory directory = new Directory();
    private final int propertyVersion = 100;

    // Function: init
    // Description: The function is used when doesn't exist the settings.properties.
    public void init(){
        // ファイルがない場合
        boolean isExist = directory.check(Paths.get(Paths.get("").toAbsolutePath()+"/settings.properties")) != 0;
        // ファイルはあるが、バージョンが古い場合
        boolean isOlder=false;
        int ver;
        if(isExist){
            Properties prop = this.getSettings();
            try {
                ver = Integer.parseInt(prop.getProperty("propertyVersion"));
                if(propertyVersion > ver){
                    isOlder = true;
                    System.out.println("Property Updated.");
                }else if(propertyVersion < ver){
                    System.out.println("Illegal property version. Please fix it.");
                    System.exit(-1);
                }
            } catch (Exception e) {
                // ここで例外が発生するならば、確実に古いプロパティを使用している
                System.out.println("Property Updated.");
                isOlder = true;
            }
        }

        if (!isExist || isOlder) {
            Properties settings = new Properties();
            settings.setProperty("maxMemory", "4");
            settings.setProperty("minMemory", "4");
            settings.setProperty("bootName", "start-demo");
            settings.setProperty("propertyVersion", String.valueOf(this.propertyVersion));

            try (FileOutputStream out = new FileOutputStream("settings.properties")) {
                settings.store(out, "Settings Properties");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Function: getPropertyVersion
    // Description: Get the property Version
    public int getPropertyVersion(){
        return this.propertyVersion;
    }

    // Function: getSettings
    // Description: Read settings.properties and return it.
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

    // Function: setMaxMemory
    // Description: Set the maximum memory size.
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

    // Function: setMinMemory
    // Description: Set the minimum memory size.
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

    // Function: getMaxMemory
    // Description: This is getter for Property name 'MaxMemory'.
    public int getMaxMemory(){
        Properties settings = this.getSettings();
        return Integer.parseInt(settings.getProperty("maxMemory"));
    }

    // Function: getMinMemory
    // Description: This is getter for Property name 'MinMemory'
    public int getMinMemory(){
        Properties settings = this.getSettings();
        return Integer.parseInt(settings.getProperty("minMemory"));
    }

}