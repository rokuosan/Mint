package io.github.rokuosan.mint.fetcher.interfaces

interface Fetcher {
    fun fetch(options: FetcherOptions?): Boolean
    fun download(options: FetcherOptions)
}
