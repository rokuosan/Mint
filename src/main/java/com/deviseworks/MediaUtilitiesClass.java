package com.deviseworks;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class MediaUtilitiesClass {
//    public void downloadFile(URL url) throws IOException {
//        String path = url.getPath();
//        String name = path.substring(path.lastIndexOf("/") +1);
//        Files.copy(url.openStream(), Paths.get(name), REPLACE_EXISTING);
//    }

    public boolean downloadFile(URL url, Path path){
        String urlPath = url.getPath();
        String name = urlPath.substring(urlPath.lastIndexOf("/") +1);
        try {
            Files.copy(url.openStream(), Paths.get(path + "/" + name), REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean checkDirectory(Path path){
        return !Files.exists(path);
    }

    public boolean createDirectory(Path path){
        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean isWindows(){
        final String OS_NAME = System.getProperty("os.name").toLowerCase();
        return OS_NAME.startsWith("windows");
    }
    public boolean isLinux(){
        final String OS_NAME = System.getProperty("os.name").toLowerCase();
        return OS_NAME.startsWith("linux");
    }
}
