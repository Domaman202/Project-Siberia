package ru.DmN.siberia

import ru.DmN.siberia.console.BaseCommands
import ru.DmN.siberia.console.BaseConsole
import ru.DmN.siberia.console.BuildCommands

object ConsoleImpl : BaseConsole() {
    @JvmStatic
    fun main(args: Array<String>) {
        commands += BaseCommands.ALL_COMMANDS
        commands += BuildCommands.ALL_COMMANDS
        run(args)
    }
}