package ru.DmN.siberia.utils.exception

import java.io.InputStream
import java.util.function.Function

class MessageException(cause: BaseException?, override val message: String) : BaseException(cause) {
    override fun print(sb: StringBuilder, provider: Function<String, InputStream>?): StringBuilder {
        sb.append(message)
        prev?.print(sb.append('\n'), provider)
        return sb
    }
}