package io.github.rokuosan.mint.fetcher

import io.github.rokuosan.mint.fetcher.interfaces.FetcherOptions
import io.github.rokuosan.mint.models.PaperBuilds
import io.github.rokuosan.mint.utils.URLProvider
import kotlinx.serialization.json.Json

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

        println("TODO")
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
