package io.github.rokuosan.mint.utils

import java.net.URL


object URLProvider {
    private const val PAPER_URL = "https://api.papermc.io/v2/projects/paper"

    fun paperVersionURL() = URL(PAPER_URL)
    fun paperBuildURL(version: String) = URL("$PAPER_URL/versions/$version")

}