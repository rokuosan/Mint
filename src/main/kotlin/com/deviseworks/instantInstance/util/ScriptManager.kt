package com.deviseworks.instantInstance.util

object ScriptManager {
    fun generatePaper(version: String, build: String){
        val os = System.getProperty("os.name")
        val isWindows = os.startsWith("windows", true)
        val script = if(isWindows){
            buildString {
                append("@echo off")
                append("java -Xmx4G -Xms4G -server -jar paper-$version-$build.jar nogui")
                append("pause")
            }
        }else{
            buildString {
                append("#!/bin/sh")
                append("java -Xmx4G -Xms4G -server -jar paper-$version-$build.jar nogui")
            }
        }

        val filename = if(isWindows){
            "start-demo.bat"
        }else{
            "start-demo.sh"
        }






    }
}