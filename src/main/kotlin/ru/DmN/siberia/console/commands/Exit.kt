package ru.DmN.siberia.console.commands

import ru.DmN.siberia.console.Console
import ru.DmN.siberia.console.utils.Command

object Exit : Command(
    null,
    null,
    "Прочее",
    "Выход",
    "Завершает работу консоли.",
    emptyList()
) {
    override fun available(console: Console): Boolean =
        console.interactive

    override fun builderAvailable(flags: Map<Any?, Any?>): Boolean =
        false

    override fun action(console: Console, vararg args: Any?) {
        Runtime.getRuntime().exit(0)
        throw Error("Выход")
    }
}