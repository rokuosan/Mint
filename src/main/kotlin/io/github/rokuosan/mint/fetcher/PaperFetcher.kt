package io.github.rokuosan.mint.fetcher

import com.github.ajalt.mordant.terminal.Terminal
import io.github.rokuosan.mint.fetcher.interfaces.FetcherOptions
import io.github.rokuosan.mint.models.PaperBuilds
import io.github.rokuosan.mint.models.PaperVersions
import io.github.rokuosan.mint.utils.URLProvider
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import java.net.URL
import java.nio.file.StandardCopyOption
import kotlin.io.path.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

data class PaperFetcherOptions(
    val version: String,
    val build: String,
    override val destination: String,
    override val filename: String? = "paper-${version}-${build}.jar"
): FetcherOptions

class PaperFetcher: AbstractFetcher() {
    override fun getFetcherInformation() = "Paper Fetcher"
    private val paperApi = "https://api.papermc.io/v2/projects/paper"
    private val terminal = Terminal()
    private val client = HttpClient{
        install(ContentNegotiation){
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = true
                encodeDefaults = true
            })
        }
    }

    private var versions: PaperVersions? = null
    private var builds: PaperBuilds? = null

    /**
     * Fetches the versions of PaperMC.
     *
     * @return PaperVersions
     */
    suspend fun getVersions(): PaperVersions {
        val res = client.get(this.paperApi)
        if (res.status.value != 200) {
            throw Exception("Failed to fetch versions.")
        }
        this.versions = res.body<PaperVersions>()
        return this.versions!!
    }

    /**
     * Fetches the builds of the specified version.
     *
     * @param version The version of PaperMC.
     * @return PaperBuilds
     */
    suspend fun getBuilds(version: String): PaperBuilds {
        if (this.versions != null) {
            val versions = this.versions!!.versions
            if (!versions.contains(version)) {
                throw Exception("Invalid version.")
            }
        }

        val res = client.get(this.paperApi + "/versions/$version")
        if (res.status.value != 200) {
            throw Exception("Failed to fetch builds.")
        }
        this.builds = res.body<PaperBuilds>()
        return this.builds!!
    }


    fun download(version: String, build: String, destination: String, filename: String? = null) {
        val url = "${this.paperApi}/versions/$version/builds/$build/downloads/paper-$version-$build.jar"
        val path = Path(destination)

        val created = if (!path.exists()) {
            path.createDirectories()
            true
        }else {
            false
        }

        val dest = path.resolve(filename ?: "paper-$version-$build.jar")

        val ok = super.download(URL(url), dest.toString(), StandardCopyOption.REPLACE_EXISTING)
        if (!ok) {
            terminal.println("Failed to download file.")
            if (created) {
                path.deleteIfExists()
            }
        } else {
            dest.toFile().setExecutable(true)
        }
    }


    fun download(options: FetcherOptions) {
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
            terminal.println("Failed to download file.")
            if (isCreated) {
                dir.deleteIfExists()
            }
        } else {
            // Add execute permission
            dest.toFile().setExecutable(true)
        }
    }
}
