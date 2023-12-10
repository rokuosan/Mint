package io.github.rokuosan.mint.fetcher

import java.net.HttpURLConnection
import java.net.URL

interface Fetcher {
    fun fetch(): Boolean
    fun download(vararg conditions: String)
}

abstract class AbstractFetcher: Fetcher {
    fun sendGetRequest(url: URL): String? {
        val con = url.openConnection() as HttpURLConnection
        // Make a connection
        con.connectTimeout = 20_000
        con.readTimeout = 20_000
        con.connect()

        // Get response
        return try {
            con.inputStream.bufferedReader().use{
                return it.readText()
            }
        }catch (e: Exception) {
            null
        }
    }
}