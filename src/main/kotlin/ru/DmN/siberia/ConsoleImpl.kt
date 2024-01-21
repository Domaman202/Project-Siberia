package ru.DmN.siberia

import ru.DmN.siberia.console.BaseCommands.CMD_BUILDER
import ru.DmN.siberia.console.BaseCommands.HELP
import ru.DmN.siberia.console.BaseConsole
import ru.DmN.siberia.console.BuildCommands.MODULE_COMPILE
import ru.DmN.siberia.console.BuildCommands.MODULE_PRINT
import ru.DmN.siberia.console.BuildCommands.MODULE_SELECT
import ru.DmN.siberia.console.BuildCommands.MODULE_UNPARSE
import ru.DmN.siberia.console.BuildCommands.PLATFORM_SELECT

object ConsoleImpl : BaseConsole() {
    @JvmStatic
    fun main(args: Array<String>) {
        commands += HELP
        commands += CMD_BUILDER
        commands += MODULE_SELECT
        commands += PLATFORM_SELECT
        commands += MODULE_UNPARSE
        commands += MODULE_PRINT
        commands += MODULE_COMPILE
        run(args)
    }
}