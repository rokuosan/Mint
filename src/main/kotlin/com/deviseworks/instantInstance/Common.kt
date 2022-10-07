package com.deviseworks.instantInstance

object Common{
    // Build Version
    private const val VERSION = "v0.2.0"
    private const val CHANNEL = "dev"
    const val BUILD = "$VERSION-$CHANNEL"

    // Prefix
    const val SHELL = "> "
    const val SHELL_INSTALL = "INSTALL $SHELL"

    // Error Message
    const val COMMAND_NOT_FOUND = "\t不明なコマンド\n\thelpでコマンドリストを表示します。"

}