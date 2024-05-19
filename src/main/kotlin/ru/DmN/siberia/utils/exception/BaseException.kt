package ru.DmN.siberia.utils.exception

import java.io.InputStream
import java.util.function.Function

abstract class BaseException(final override val cause: Throwable?) : Exception(cause) {
    open val prev: BaseException? =
        if (cause is BaseException) cause else null

    fun print(provider: Function<String, InputStream>?): String =
        print(StringBuilder(), provider).toString()

    abstract fun print(sb: StringBuilder, provider: Function<String, InputStream>?): StringBuilder
}