package io.github.rokuosan.mint

import com.github.ajalt.clikt.core.subcommands
import io.github.rokuosan.mint.commands.New

fun main(args: Array<String>) = Mint()
    .subcommands(New())
    .main(args)
