package ru.DmN.siberia.console

import ru.DmN.siberia.console.commands.CmdBuilder
import ru.DmN.siberia.console.commands.Help

object BaseCommands {
    @JvmStatic
    val ALL_COMMANDS = listOf(Help, CmdBuilder)
}