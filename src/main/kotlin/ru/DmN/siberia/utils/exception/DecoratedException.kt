package ru.DmN.siberia.utils.exception

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.PrintStream
import java.util.function.Function

class DecoratedException(really: Throwable) : BaseException(really) {
    override fun print(sb: StringBuilder, provider: Function<String, InputStream>?): StringBuilder {
        val stream = ByteArrayOutputStream()
        cause!!.printStackTrace(PrintStream(stream))
        return sb.append(stream.toString())
    }
}