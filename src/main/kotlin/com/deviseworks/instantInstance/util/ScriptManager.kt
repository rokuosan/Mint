package com.deviseworks.instantInstance.util

import java.io.BufferedWriter
import java.io.FileWriter

object ScriptManager {
    fun generatePaper(version: String, build: String, path: String){
        // Checking OS
        val os = System.getProperty("os.name")
        val isWindows = os.startsWith("windows", true)

        // Create script for booting paper
        val script = if(isWindows){
            buildString {
                append("@echo off")
                appendLine()
                append("java -Xmx4G -Xms4G -server -jar paper-$version-$build.jar nogui")
                appendLine()
                append("pause")
            }
        }else{
            buildString {
                append("#!/bin/sh")
                appendLine()
                append("java -Xmx4G -Xms4G -server -jar paper-$version-$build.jar nogui")
            }
        }

        // Script's extension
        val filename = if(isWindows){
            "$path/start-demo.bat"
        }else{
            "$path/start-demo.sh"
        }

        // Flush
        BufferedWriter(FileWriter(filename)).use { bw ->
            bw.write(script)
            bw.flush()
        }
    }
}