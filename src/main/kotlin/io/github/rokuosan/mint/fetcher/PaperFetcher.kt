package io.github.rokuosan.mint.fetcher

import io.github.rokuosan.mint.TERMINAL
import io.github.rokuosan.mint.fetcher.interfaces.FetcherOptions
import io.github.rokuosan.mint.models.PaperBuilds
import io.github.rokuosan.mint.utils.URLProvider
import kotlinx.serialization.json.Json
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

data class PaperFetcherOptions(
    val version: String,
    val build: String,
    override val destination: String,
    override val filename: String?
): FetcherOptions

class PaperFetcher: AbstractFetcher() {
    override fun fetch(options: FetcherOptions?): Boolean {
        return false
    }

    override fun download(options: FetcherOptions) {
        if (options !is PaperFetcherOptions) return
        val url = URLProvider.paperDownloadURL(options.version, options.build)
        val filename = if (options.filename != null) {
            options.filename + ".jar"
        }else {
            "paper-${options.version}-${options.build}.jar"
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
        val ok = super.download(url, dest.toString(), StandardCopyOption.REPLACE_EXISTING)
        if (!ok) {
            TERMINAL.println("Failed to download file.")
            if (isCreated) {
                dir.deleteIfExists()
            }
        } else {
            // Add execute permission
            dest.toFile().setExecutable(true)
        }
    }

    fun getBuilds(version: String): List<Int>? {
        return try {
            val body = sendGetRequest(URLProvider.paperBuildURL(version))?:return null
            val json = Json.decodeFromString<PaperBuilds>(body)
            json.builds
        }catch (e: Exception){
            null
        }
    }
}
