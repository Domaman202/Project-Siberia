package ru.DmN.siberia.utils.exception

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream
import java.util.function.Function

class DecoratedException(private val really: Throwable) : BaseException(null) {
    override fun print(sb: StringBuilder, provider: Function<String, InputStream>?): StringBuilder {
        val stream = ByteArrayOutputStream()
        really.printStackTrace(PrintStream(stream))
        return sb.append(stream.toString())
    }
}