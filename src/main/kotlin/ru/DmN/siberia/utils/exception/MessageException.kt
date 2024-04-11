package ru.DmN.siberia.utils.exception

import java.io.InputStream
import java.util.function.Function

class MessageException(prev: BaseException?, override val message: String) : BaseException(prev) {
    override fun print(sb: StringBuilder, provider: Function<String, InputStream>?): StringBuilder {
        sb.append(message)
        prev?.print(sb.append('\n'), provider)
        return sb
    }
}