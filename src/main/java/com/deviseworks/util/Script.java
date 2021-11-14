package com.deviseworks.util;

import com.deviseworks.actions.Settings;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Script {
    // Function: generate
    // Description: Generate a script to boot the server software.
    public boolean generate(Path path, String fileName){
        Machine machine = new Machine();
        Settings settings = new Settings();

        // 引数のリスト
        List<String> args = new ArrayList<>();

        // プロパティ
        Properties property = settings.getSettings();
        // ファイルの名前を定義
        String name = property.getProperty("bootName");
        if(name == null){
            name = "start-demo";
            System.out.println("[E] Boot name is not defined. Use default name.(start-demo)");
        }
        // ファイルの拡張子を定義
        String ext;
        if(machine.isWindows()){
            ext = ".bat";
        }else{
            ext = ".sh";
        }

        // 合成してファイルにする
        File out = new File(path.toString() + "/" +name + ext);

        // 各メモリ量を取得
        int maxMemory = settings.getMaxMemory();
        int minMemory = settings.getMinMemory();
        String xmx = "-Xmx" + maxMemory + "G";
        String xms = "-Xms" + minMemory + "G";

        args.add(xmx);
        args.add(xms);
        args.add("-server");
        args.add("-jar");
        args.add(fileName);
        args.add("nogui");
        if(machine.isWindows()){
            args.add("\r\n");
            args.add("pause");
        }else{
            args.add("\n");
        }

        // スクリプト作成
        StringBuilder script = new StringBuilder();
        if(machine.isWindows()){
            script.append("@echo off\r\njava ");
        }else {
            script.append("#!/bin/bash\njava ");
        }
        for(String s: args){
            script.append(s);
            script.append(" ");
        }

        // ファイルへ書き込み
        try{
            Files.createFile(out.toPath());
            FileWriter fw = new FileWriter(out);
            fw.write(script.toString());
            fw.close();
        } catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
