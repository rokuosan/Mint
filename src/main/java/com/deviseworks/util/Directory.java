package com.deviseworks.util;

import net.lingala.zip4j.ZipFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class Directory {
    // Function: check()
    // Argument: Path
    // Description: Check if the directory exists. If it doesn't exist, return 0.
    public int check(Path path){
        if(Files.exists(path)){
            if(Files.isDirectory(path)){
                return 2; // Directory
            }else if(Files.isSymbolicLink(path)){
                return 3; // SymbolicLink
            }else{
                return 1; // Files
            }
        }

        return 0;
    }

    // Function: create()
    // Argument: Path
    // Description: Create the directory.
    public boolean create(Path path){
        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Function: remove()
    // Argument: Path
    // Description: Remove the directory
    public boolean remove(Path path){
        int type = this.check(path); // Files=1, Directory=2, SymbolicLink=3

        if(type==2){
            try {
                Stream<Path> files = Files.list(path);  // リスト取得
                files.forEach(this::remove);            // それがディレクトリなら再帰的に削除
                Files.delete(path);                     // 自分を削除
            } catch (IOException e) {
                return false;
            }
        }else{
            try {
                Files.delete(path);
            } catch (IOException e) {
                return false;
            }
        }

        return true; // Doesn't Exists
    }

    // Function: seek
    // Argument: Path
    // Description: Find the file or directory.
    public Path seek(Path path, String software, String version){
        int tag = 100;
        Path check;

        while(true){
            check = Paths.get(path + "/" + software + "/" + version + "/" + tag + "/");
            if(this.check(check) == 0){ //ディレクトリが存在しない場合 == 作れる場合
                return check;
            }else{
                tag++;
            }
        }
    }

    public Path seek(Path path, String software){
        int tag = 100;
        Path check;

        while(true){
            check = Paths.get(path + "/" + software  + "/" + tag + "/");
            if(this.check(check) == 0){ //ディレクトリが存在しない場合 == 作れる場合
                return check;
            }else{
                tag++;
            }
        }
    }

    // Function: unzip
    // Argument: filePath
    public boolean unzip(Path filePath){
        try{
            String dest = filePath.toString().substring(0, filePath.toString().lastIndexOf("\\"));
            new ZipFile(filePath.toString()).extractAll(dest);
            this.remove(filePath);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    // Function: unTarGz
    // Argument: filePath
    public boolean unTarGz(Path filePath){
        try{
            String dest = filePath.toString().substring(0, filePath.toString().lastIndexOf("/")+1);
            Runtime runtime = Runtime.getRuntime();
            Process result = runtime.exec("tar -zxvf " + filePath + " -C " + dest);

//            もしログに残すならこれを利用してね
//            BufferedReader br = new BufferedReader(new InputStreamReader(result.getInputStream()));
//            while(true){
//                String line = br.readLine();
//                if(line == null){
//                    break;
//                }
//                System.out.println(line);
//            }

            this.remove(filePath);

            return true;
        }catch(Exception e){
            return false;
        }
    }
}
