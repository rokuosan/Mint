package io.github.rokuosan.mint

import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.terminal.Terminal
import io.github.rokuosan.mint.commands.New

val TERMINAL = Terminal(ansiLevel = AnsiLevel.TRUECOLOR, interactive = true)

fun main(args: Array<String>) = Mint()
    .context {
        terminal = TERMINAL
    }
    .subcommands(New())
    .main(args)