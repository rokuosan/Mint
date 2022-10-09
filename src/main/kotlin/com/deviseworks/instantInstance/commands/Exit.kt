package com.deviseworks.instantInstance.commands

import kotlin.system.exitProcess

class Exit: CommandInterface {
    override fun interact() {
        exitProcess(0)
    }
}