package ru.DmN.siberia.console.utils

import ru.DmN.siberia.console.Console

fun interface CommandAction {
    operator fun invoke(console: Console, vararg args: Any?)
}