package com.deviseworks.instantInstance

import com.deviseworks.instantInstance.commands.Exit
import com.deviseworks.instantInstance.commands.Install

/**
 * 実行可能なコマンドの列挙
 */
enum class CommandList(
        val function: () -> Unit = { }
){
    INSTALL({ Install().interact() }),
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
        if (call.isNotBlank() && !hit){
            println(Common.COMMAND_NOT_FOUND)
        }

        // 再起呼び出し
        print(Common.SHELL)
        val command = readLine()?:""
        next(command)
    }

}