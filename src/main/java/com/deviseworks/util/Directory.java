package com.deviseworks.util;

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

    // Function: search
    // Argument: Path
    // Description: Find the file or directory.
//    public boolean seek(String name, Path path){
//        int tag = 100;
//        Path check;
//
//        while(true){
//            check = Paths.get(path + name)
//        }
//    }
}
