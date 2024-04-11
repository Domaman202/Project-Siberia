package ru.DmN.siberia.utils.exception

import java.io.InputStream
import java.util.function.Function

abstract class BaseException(open val prev: BaseException?) : Exception() {
    fun print(provider: Function<String, InputStream>?): String =
        print(StringBuilder(), provider).toString()

    abstract fun print(sb: StringBuilder, provider: Function<String, InputStream>?): StringBuilder
}