package ru.DmN.siberia.console.utils

import ru.DmN.siberia.console.Console

fun interface CommandCheck {
    operator fun invoke(console: Console): Boolean
}