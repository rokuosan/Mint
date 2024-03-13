package io.github.rokuosan.mint.fetcher

import com.github.ajalt.mordant.terminal.Terminal
import io.github.rokuosan.mint.fetcher.interfaces.FetcherOptions
import io.github.rokuosan.mint.models.PaperBuilds
import io.github.rokuosan.mint.models.PaperVersions
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.net.URL
import java.nio.file.StandardCopyOption
import kotlin.io.path.*

data class PaperFetcherOptions(
    val version: String,
    val build: String,
    override val destination: String,
    override val filename: String? = "paper-${version}-${build}.jar"
): FetcherOptions

private const val API_ENDPOINT = "https://api.papermc.io/v2/projects/paper"

class PaperFetcher(
    private val options: PaperFetcherOptions
): AbstractFetcher() {
    override fun getFetcherInformation() = "Paper Fetcher"
    private val terminal = Terminal()

    companion object {
        private val client =  HttpClient{
            install(ContentNegotiation){
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    encodeDefaults = true
                })
            }
        }

        /**
         * Fetches the versions of PaperMC.
         *
         * @return PaperVersions
         */
        suspend fun getVersion(): PaperVersions {
            val res = client.get(API_ENDPOINT)
            if (res.status.value != 200) {
                throw Exception("Failed to fetch versions.")
            }
            return res.body<PaperVersions>()
        }


        /**
         * Fetches the builds of the specified version.
         *
         * @param version The version of PaperMC.
         * @return PaperBuilds
         */
        suspend fun getBuilds(version: String): PaperBuilds {
            val res = client.get("$API_ENDPOINT/versions/$version")
            if (res.status.value != 200) {
                throw Exception("Failed to fetch builds.")
            }
            return res.body<PaperBuilds>()
        }
    }

    private fun download() {
        val defaultName = "paper-${options.version}-${options.build}.jar"

        val url = "${API_ENDPOINT}/versions/${options.version}" +
                "/builds/${options.build}/downloads/${defaultName}"
        val filename = if (options.filename != null) {
            options.filename + ".jar"
        }else {
            defaultName
        }

        // Create directories
        var isCreated = false
        val dir = Path(options.destination)
        if (!dir.exists()) {
            dir.createDirectories()
            isCreated = true
        }

        // Download file
        val dest = dir.resolve(filename)
        val ok = super.download(URL(url), dest.toString(), StandardCopyOption.REPLACE_EXISTING)
        if (!ok) {
            terminal.println("Failed to download file.")
            if (isCreated) {
                dir.deleteIfExists()
            }
        } else {
            // Add execute permission
            dest.toFile().setExecutable(true)
        }
    }

    private fun createSnippet() {
        val filename = "paper-${options.version}-${options.build}.jar"
        val shell = """
                #!/bin/bash
                java -Xms4G -Xmx4G -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -jar $filename
                """.trimIndent()
        val batch = """
                @echo off
                java -Xms4G -Xmx4G -XX:+UnlockExperimentalVMOptions -XX:+DisableExplicitGC -jar $filename
                """.trimIndent()

        val dir = Path(options.destination)
        dir.resolve("start.sh").apply {
            this.writeText(shell)
            this.toFile().setExecutable(true)
        }
        dir.resolve("start.bat").writeText(batch)
    }

    fun install() {
        this.download()
        this.createSnippet()
    }
}
