package com.deviseworks.actions;

import com.deviseworks.util.Directory;
import com.deviseworks.util.Internet;
import com.deviseworks.util.Machine;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Cuberite {
    private final Machine machine = new Machine();
    private final Internet internet = new Internet();
    private final Directory directory = new Directory();
    private String extType = null;

    // Function: setLink()
    // Description: Set the link to download Cuberite Archive.
    public URL setLink(){
        // OSを確定する。Windows以外の場合はLinuxとして扱う。Macユーザーは知らね。
        String machineType;
        if(machine.isWindows()){
            machineType = "windows";
            extType = ".zip";
        }else{
            machineType = "linux";
            extType = ".tar.gz";
        }

        // アーキテクチャを確定する。判定できない場合は32bitとして扱う。
        String machineArch;
        if(machine.is64bit()){
            machineArch = "x86_64";
        }else{
            machineArch = "i386";
        }

        // リンクを確定する
        String downloadLink = "https://download.cuberite.org/" + machineType + "-" + machineArch + "/Cuberite" + extType;
        try {
            return new URL(downloadLink);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean download(Path path){
        URL url = this.setLink();

        if(url == null){
            return false;
        }

        return internet.downloadFile(url, path);
    }

    public boolean install(Path path){
        if(this.download(path)){
            path = Paths.get(path + "/Cuberite" + extType);
            if(machine.isWindows()){
                return directory.unzip(path);
            }else{
                return directory.unTarGz(path);
            }
        }

        return false;
    }

}
