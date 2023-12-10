package io.github.rokuosan.mint.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.mordant.table.table
import io.github.rokuosan.mint.fetcher.PaperFetcher

class New: CliktCommand() {
    private val engine by option("-e", "--engine", help = "Choose server engine")
        .choice("vanilla", "paper")
        .required()
    private val version by option("-v", "--version", help = "Choose minecraft version")
        .required()

    override fun run() {
        when (engine) {
            "paper", "p" -> {
                echo(table {
                    body { row("Paper", version) }
                })
                echo("Checking builds for version $version")
                val builds = PaperFetcher().getBuilds(version)?:run{
                    echo("Invalid version")
                    return
                }
                echo("Found ${builds.size} builds.")
            }
            "vanilla", "v" -> {
                echo("Not implemented.")
            }
        }
    }

}