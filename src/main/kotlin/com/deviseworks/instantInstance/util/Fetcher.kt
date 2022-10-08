package com.deviseworks.instantInstance.util

import com.deviseworks.instantInstance.Common
import com.deviseworks.instantInstance.entity.PaperVersions
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL

class Fetcher {
    fun getPaperVersions(): PaperVersions?{
        val connect = URL(Common.PAPER_VERSIONS).openConnection() as HttpURLConnection
        connect.connectTimeout = 20_000
        connect.readTimeout = 20_000
        connect.connect()

        return try{
            val sb = StringBuilder()
            connect.inputStream.bufferedReader().use { br ->
                for(l in br.lines()){
                    sb.append(l)
                }
            }

            Json.decodeFromString<PaperVersions>(sb.toString())
        }catch(e: Exception){
            e.printStackTrace()
            println("Connection Error: Failed to fetch")
            null
        }
    }
}