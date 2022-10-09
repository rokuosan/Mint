package com.deviseworks.instantInstance

object Common{
    // Build Version
    private const val VERSION = "v0.2.0"
    private const val CHANNEL = "dev"
    const val BUILD = "$VERSION-$CHANNEL"

    // Prefix
    const val SHELL = "> "
    const val SHELL_INSTALL = "INSTALL $SHELL"
    const val SHELL_INSTALL_PAPER = "${SHELL_INSTALL}PAPER > "

    // Error Message
    const val COMMAND_NOT_FOUND = "\t不明なコマンド\n\thelpでコマンドリストを表示します。"
    const val INVALID_VALUE = "\t不明な値です"

    // API URL
    const val PAPER_VERSIONS = "https://api.papermc.io/v2/projects/paper"

    // Directories


}