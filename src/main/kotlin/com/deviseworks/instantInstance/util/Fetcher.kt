package com.deviseworks.instantInstance.util

import com.deviseworks.instantInstance.Common
import com.deviseworks.instantInstance.entity.PaperBuilds
import com.deviseworks.instantInstance.entity.PaperVersions
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

object Fetcher {
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

    fun getPaperBuilds(version: String): PaperBuilds?{
        val c = URL("${Common.PAPER_VERSIONS}/versions/$version").openConnection() as HttpURLConnection
        c.connect()

        return try{
            val sb = StringBuilder()
            c.inputStream.bufferedReader().use { br ->
                br.lines().forEach {
                    sb.append(it)
                }
            }

            Json.decodeFromString<PaperBuilds>(sb.toString())
        }catch(e: Exception){
            e.printStackTrace()
            println("Connection Error: Failed to fetch build")
            null
        }
    }

    /**
     * ファイルをダウンロードする関数
     *
     * @param url ダウンロードURL
     * @param dest ダウンロード先
     */
    private fun downloadFile(url: String, dest: Path){
        val filename = url.substring(url.lastIndexOf("/")+1)

        try{
            Files.copy(URL(url).openStream(), Paths.get("${dest}/$filename"), StandardCopyOption.REPLACE_EXISTING)
        }catch(e: Exception){
            e.printStackTrace()
        }
    }

    fun downloadPaper(version: String, build: String, dest: Path){
        val url = "https://api.papermc.io/v2/projects/paper/versions/$version/builds/$build/downloads/paper-$version-$build.jar"
        this.downloadFile(url, dest)
    }
}