package io.github.rokuosan.mint.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.mordant.animation.progressAnimation
import com.github.ajalt.mordant.terminal.StringPrompt
import com.github.ajalt.mordant.widgets.Spinner
import io.github.rokuosan.mint.TERMINAL
import io.github.rokuosan.mint.fetcher.PaperFetcher
import io.github.rokuosan.mint.fetcher.PaperFetcherOptions
import io.github.rokuosan.mint.utils.RandomStringProvider
import kotlinx.coroutines.*

class New: CliktCommand() {
    private val engine by option("-e", "--engine", help = "Server engines(e.g. Paper, Vanilla)")
        .choice("vanilla", "paper")
        .required()
    private val version by option("-v", "--version", help = "Minecraft versions (e.g. \"latest\", \"1.20.2\")")
        .required()
    private val build by option("-b", "--build", help = "Build number (e.g. \"latest\", \"1\")")

    private val destination by option("-d", "--destination", help = "Output directory")
    private val filename by option("-f", "--filename", help = "Output filename")

    override fun run() = runBlocking {
        when (engine) {
            "paper", "p" -> {
                val terminal = TERMINAL
                val bld = if (build != null) {
                    build!!
                }else {
                    // Make a progress bar
                    val progress = terminal.progressAnimation {
                        text("Fetching builds...")
                        spinner(Spinner.Dots())
                    }
                    progress.start()

                    // Fetch builds
                    var bld: List<Int>? = null
                    val job = launch {
                        bld = PaperFetcher().getBuilds(version) ?: emptyList()
                    }

                    // Wait for fetching builds
                    while (bld == null) {
                        delay(10)
                        progress.advance(1)
                    }

                    // Stop progress bar
                    job.join()
                    progress.stop()
                    terminal.cursor.move {
                        up(1)
                        clearLineAfterCursor()
                    }
                    terminal.println("Fetching builds... Done")

                    if (bld!!.isEmpty()) {
                        echo("No builds found.")
                        return@runBlocking
                    }
                    bld!!.last().toString()
                }

                val dest = if (destination != null) {
                    destination!!
                }else {
                    RandomStringProvider.getShortString()
                }

                val pds = StringPrompt(
                    prompt = "Destination",
                    terminal = terminal,
                    default = dest,
                    showDefault = true,
                ).ask()?:dest
                echo("Downloading Paper build $bld[MC:$version] to $pds...")
                PaperFetcher().download(PaperFetcherOptions(version, bld, dest, filename))
            }
            "vanilla", "v" -> {
                echo("Not implemented.")
            }
        }
    }

}