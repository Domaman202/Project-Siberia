package ru.DmN.siberia

import ru.DmN.siberia.console.BaseCommands.HELP
import ru.DmN.siberia.console.BaseConsole
import ru.DmN.siberia.console.BuildCommands.MODULE_COMPILE
import ru.DmN.siberia.console.BuildCommands.MODULE_PRINT
import ru.DmN.siberia.console.BuildCommands.MODULE_RUN
import ru.DmN.siberia.console.BuildCommands.MODULE_RUN_TEST
import ru.DmN.siberia.console.BuildCommands.MODULE_SELECT

object ConsoleImpl : BaseConsole() {
    @JvmStatic
    fun main(args: Array<String>) {
        commands += HELP
        commands += MODULE_SELECT
        commands += MODULE_PRINT
        commands += MODULE_COMPILE
        commands += MODULE_RUN
        commands += MODULE_RUN_TEST
        run(args)
    }
}