package io.github.rokuosan.mint.helper

import com.github.ajalt.mordant.animation.animation
import com.github.ajalt.mordant.table.horizontalLayout
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.widgets.Spinner
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel

fun <T> Terminal.loading(loadingMessage: String = "Loading...", func: suspend () -> T): T = runBlocking {
    val spinner = Spinner.Dots()
    val animation = this@loading.animation<Int> {
        horizontalLayout {
            cell(loadingMessage)
            cell(spinner)
        }
    }
    val updater = launch {
        while (true) {
            animation.update(spinner.advanceTick())
            delay(125)
        }
    }
    val ch = Channel<T>()
    launch(context = Dispatchers.IO) {
        ch.send(func())
    }
    val response = ch.receive()
    updater.cancelAndJoin()
    animation.clear()
    ch.close()

    return@runBlocking response
}