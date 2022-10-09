package com.deviseworks.instantInstance.commands

import com.deviseworks.instantInstance.Common
import com.deviseworks.instantInstance.util.Fetcher
import com.deviseworks.instantInstance.util.FileManager
import com.deviseworks.instantInstance.util.ScriptManager
import java.nio.file.Files
import java.nio.file.Paths

class Install: CommandInterface{
    override fun interact(){
        next()
    }

    private fun next(call: String = ""){
        when(call.lowercase()){
            "paper", "p" -> paper()
            "cuberite", "c" -> cuberite()
            "exit", "q" -> return
            "help" -> help()
            else -> {
                if(call.isNotBlank()) println(Common.COMMAND_NOT_FOUND)
            }
        }

        print(Common.SHELL_INSTALL)
        val command = readLine()?:""
        next(command)
    }

    private fun help(){
        println("""
            
            利用可能なソフトウェア一覧
            - Paper
            
            実行可能なコマンド
            - paper, p     : Paper インストールウィザードを開始します。
            - cuberite, c  : Cuberite インストールウィザードを開始します。
            - help
            - exit
            
        """.trimIndent())
    }

    private fun paper(){
        println("Paper インストールウィザードを開始します...")
        val pv =Fetcher.getPaperVersions() ?: return

        // バージョンを選択する
        var version = ""
        println("""
            
            - バージョンを入力してください。
            - latest と入力することで最新版を選択できます。
        """.trimIndent())
        while(version == ""){
            print(Common.SHELL_INSTALL_PAPER)
            val command = readLine()

            if(command.equals("latest", true)){
                version = pv.versions.last()
                println("- バージョン$version を選択しました\n")
                break
            }

            for(v in pv.versions){
                if(v == command){
                    version = v
                    break
                }
            }
            if(version.isBlank()) println(Common.INVALID_VALUE)
        }

        // ビルドを選択する
        val pb = Fetcher.getPaperBuilds(version)?:return
        var build = ""
        println("""
            - ビルド番号を入力してください。
            - latest と入力することで最新ビルドを選択できます。
        """.trimIndent())
        while(build == ""){
            print(Common.SHELL_INSTALL_PAPER)
            val command = readLine()

            if(command.equals("latest", true)){
                build = pb.builds.last().toString()
                println("- ビルド番号$build を選択しました")
                break
            }

            pb.builds.forEach { b ->
                if(b.toString() == command){
                    build = b.toString()
                }
            }
            if(build.isBlank()) println(Common.INVALID_VALUE)
        }

        // Reserve directory
        val tag = FileManager.seekNumber("paper", version)
        val dir = Paths.get("./paper/$version/$tag/").toAbsolutePath().normalize()

        // Confirm
        println("\nPaper を以下の条件でインストールします")
        println("\t- Version : $version")
        println("\t- Build   : $build")
        println("\t- Location: $dir\n")

        print("よろしいですか？[y/N]: ")

        if(!readLine().equals("y", true)){
            println("キャンセルしました")
            return
        }

        // Install
        try{
            Files.createDirectories(dir)
        }catch (e: Exception){
            e.stackTrace
            println(Common.FAILED_TO_CREATE_FOLDER)
            return
        }
        Fetcher.downloadPaper(version, build, dir)

        // Generate script
        try {
            ScriptManager.generatePaper(version, build, dir.toString())
        } catch(e: Exception) {
            println("I/O Error: Couldn't write shell script")
            return
        }

        // Finish
        println("Complete!\n")
    }

    private fun cuberite(){
        println("未実装です")
    }
}