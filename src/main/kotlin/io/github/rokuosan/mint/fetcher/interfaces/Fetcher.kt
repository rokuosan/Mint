package io.github.rokuosan.mint.fetcher.interfaces

interface Fetcher {
    val options: FetcherOptions
    fun getFetcherInformation(): String
}
