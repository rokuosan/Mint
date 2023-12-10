package io.github.rokuosan.mint.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice

class New: CliktCommand() {
    private val engine by option("-e", "--engine", help = "Choose server engine")
        .choice("vanilla", "paper")
        .required()
    private val version by option("-v", "--version", help = "Choose minecraft version")
        .required()

    override fun run() {
        when (engine) {
            "paper", "p" -> {
                echo()
            }
            "vanilla", "v" -> {
                echo("Not implemented.")
            }
        }
    }

}