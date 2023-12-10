package io.github.rokuosan.mint.fetcher

import io.github.rokuosan.mint.models.PaperBuilds
import io.github.rokuosan.mint.utils.URLProvider
import kotlinx.serialization.json.Json

class PaperFetcher: AbstractFetcher() {
    override fun fetch(): Boolean {
        return false
    }

    override fun download(vararg conditions: String) {

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