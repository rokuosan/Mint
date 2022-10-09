package com.deviseworks.instantInstance

import java.io.PrintWriter

fun main(args: Array<String>) {
    // 対話モード
    if(args.isEmpty()) {
        // 起動時メッセージ
        val pw = PrintWriter(System.out)
        pw.println("Instant Instance ${Common.BUILD}")
        pw.println("- help で実行可能なコマンドを表示します")
        pw.appendLine()
        pw.flush()

        // 対話型で起動する
        Interact().next()
    }

    // コマンドラインモード



}
