package com.deviseworks.instantInstance.util

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class FileManager {
    /**
     * カレントディレクトリから引数に対応するソフトウェアに対して
     * 作成するインスタンス番号を見つけます。
     *
     * @param software
     */
    fun seekNumber(software: String, version: String): Int{
        var tag = 100
        while(true){
            val path = Paths.get("./${software}/${version}/${tag}/")
            if(Files.exists(path)){
                tag++
            }else{
                return tag
            }
        }
    }

    fun createInstance(){

    }
}