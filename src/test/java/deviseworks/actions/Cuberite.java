package deviseworks.actions;

import deviseworks.util.Directory;
import deviseworks.util.Internet;
import deviseworks.util.Machine;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Cuberite {
    Machine machine = new Machine();
    Internet internet = new Internet();
    Directory directory = new Directory();
    String machineType = null;
    String machineArch = null;
    String extType = null;
    String downloadLink = null;

    // Function: setLink()
    // Description: Set the link to download Cuberite Archive.
    public URL setLink(){
        // OSを確定する。Windows以外の場合はLinuxとして扱う。Macユーザーは知らね。
        if(machine.isWindows()){
            machineType = "windows";
            extType = ".zip";
        }else{
            machineType = "linux";
            extType = ".tar.gz";
        }

        // アーキテクチャを確定する。判定できない場合は32bitとして扱う。
        if(machine.is64bit()){
            machineArch = "x86_64";
        }else{
            machineArch = "i386";
        }

        // リンクを確定する
        downloadLink = "https://download.cuberite.org/" + machineType + "-" + machineArch + "/Cuberite" + extType;
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
