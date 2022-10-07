package com.deviseworks.instantInstance

import com.deviseworks.instantInstance.commands.Exit

/**
 * 実行可能なコマンドの列挙
 */
enum class CommandList(
        val function: () -> Unit = { }
){
    INSTALL({ }),
    UNINSTALL({}),
    HELP({}),
    SETTINGS({}),
    EXIT({ Exit().interact() });

    fun run(): Unit = function()
}

/**
 * 対話型の操作を提供するクラス
 */
class Interact {

    /**
     * 次の命令を読み取る
     */
    fun next(call: String = ""){
        // 解析
        var hit = false
        CommandList.values().forEach {
            if(it.name.equals(call, true)){
                it.run()
                hit=true
            }
        }
        if (!hit){
            if(call != ""){
                println("Unknown command.")
            }
            print("> ")
        }

        // 再起呼び出し
        val command = readLine()?:""
        next(command)
    }

}