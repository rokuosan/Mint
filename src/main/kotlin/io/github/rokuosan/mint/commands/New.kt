package io.github.rokuosan.mint.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.mordant.table.table
import io.github.rokuosan.mint.fetcher.PaperFetcher
import io.github.rokuosan.mint.fetcher.PaperFetcherOptions
import io.github.rokuosan.mint.utils.RandomStringProvider

class New: CliktCommand() {
    private val engine by option("-e", "--engine", help = "Server engines(e.g. Paper, Vanilla)")
        .choice("vanilla", "paper")
        .required()
    private val version by option("-v", "--version", help = "Minecraft versions (e.g. \"latest\", \"1.20.2\")")
        .required()
    private val build by option("-b", "--build", help = "Build number (e.g. \"latest\", \"1\")")

    private val destination by option("-d", "--destination", help = "Output directory")
    private val filename by option("-f", "--filename", help = "Output filename")

    override fun run() {
        when (engine) {
            "paper", "p" -> {
                echo(table {
                    body { row("Paper", version) }
                })
                val bld = if (build != null) {
                    build!!
                }else {
                    echo("Checking builds for version $version")
                    val builds = PaperFetcher().getBuilds(version)?:run{
                        echo("Invalid version")
                        return
                    }
                    echo("Found ${builds.size} builds.")
                    builds.last().toString()
                }

                val dest = if (destination != null) {
                    destination!!
                }else {
                    echo("Using random string as destination directory name.")
                    val randomString = RandomStringProvider.getShortString()
                    echo("Random string: $randomString")
                    randomString
                }

                echo("Downloading Paper $version build $bld to $dest")
                PaperFetcher().download(PaperFetcherOptions(version, bld, dest, filename))
            }
            "vanilla", "v" -> {
                echo("Not implemented.")
            }
        }
    }

}