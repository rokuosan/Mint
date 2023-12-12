package io.github.rokuosan.mint.fetcher

import io.github.rokuosan.mint.fetcher.interfaces.Fetcher
import java.net.HttpURLConnection
import java.net.URL

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