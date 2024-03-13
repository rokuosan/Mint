package io.github.rokuosan.mint.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.mordant.animation.progressAnimation
import com.github.ajalt.mordant.terminal.StringPrompt
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.widgets.Spinner
import io.github.rokuosan.mint.fetcher.PaperFetcher
import io.github.rokuosan.mint.fetcher.PaperFetcherOptions
import io.github.rokuosan.mint.helper.loading
import io.github.rokuosan.mint.utils.RandomStringProvider
import kotlinx.coroutines.*
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.writeText

class New: CliktCommand() {
    private val engine by option("-e", "--engine", help = "Server engines(e.g. Paper, Vanilla)")
        .choice("vanilla", "paper")
        .required()
    private val version by option("-v", "--version", help = "Minecraft versions (e.g. \"latest\", \"1.20.2\")")
        .required()
    private val build by option("-b", "--build", help = "Build number (e.g. \"latest\", \"1\")")

    private val destination by option("-d", "--destination", help = "Output directory")
    private val filename by option("-f", "--filename", help = "Output filename without extension")
    private val yes by option("-y", "--yes", help = "Skip confirmation").flag()

    override fun run() = runBlocking {
        when (engine) {
            "paper", "p" -> {
                val terminal = Terminal()
                val bld = if (build != null) {
                    build!!
                }else {
                    val bld = terminal.loading("Fetching builds ") {
                        return@loading PaperFetcher().getBuilds(version)
                    }
                    "latest"
                }

                val dest = if (destination != null) {
                    val dir = Path(destination!!)
                    if (!dir.exists()) {
                        destination!!
                    }else {
                        echo("Directory already exists.")
                        return@runBlocking
                    }
                }else {
                    var d: String? = null
                    while (d == null) {
                        // Generate random string
                        val rnd = RandomStringProvider.getShortString()

                        // Check if the directory exists
                        val dir = Path(rnd)
                        if (dir.exists()) {
                            continue
                        }

                        // Set the directory
                        d = rnd
                    }
                    "$engine-$version-$d"
                }

                val pds = if (destination != null) {
                    destination!!
                }else {
                    if (yes) {
                        dest
                    }else {
                        StringPrompt(
                            prompt = "Destination",
                            terminal = terminal,
                            default = dest,
                            showDefault = true,
                        ).ask() ?: dest
                    }
                }
                echo("Downloading Paper build $bld[MC:$version] to $pds...")
                PaperFetcher().download(PaperFetcherOptions(version, bld, pds, filename))

                // Create snippet
                val shell = """
                #!/bin/bash
                java -Xms4G -Xmx4G -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -jar paper-$version-$bld.jar
                """.trimIndent()
                val batch = """
                @echo off
                java -Xms4G -Xmx4G -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -jar paper-$version-$bld.jar
                """.trimIndent()

                // Create file
                val path = Path(pds)
                val shellFile = path.resolve("start.sh")
                shellFile.writeText("$shell\n")
                shellFile.toFile().setExecutable(true)
                val batchFile = path.resolve("start.bat")
                batchFile.writeText("$batch\n")

                echo("Completed.")
            }
            "vanilla", "v" -> {
                echo("Not implemented.")
            }
        }
    }

}