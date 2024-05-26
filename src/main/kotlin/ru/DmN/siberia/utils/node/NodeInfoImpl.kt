package ru.DmN.siberia.utils.node

import ru.DmN.siberia.utils.readBytes
import java.io.InputStream
import java.util.function.Function

/**
 * Стандартная реализация INodeInfo.
 */
class NodeInfoImpl(override val type: INodeType, override val ti: ITokenInfo?) : INodeInfo {
    override fun withType(type: INodeType): INodeInfo =
        NodeInfoImpl(type, ti?.copy())

    override fun print(message: String, provider: Function<String, InputStream>?): String =
        "$message:\n${print(provider)}"

    override fun print(provider: Function<String, InputStream>?): String =
        if (provider == null)
            ti?.run {
                """
                    [
                    | type: $type
                    | file: $file
                    | line: ${line.inc()}
                    | sym:  $symbol
                    | len:  $length
                    ]
                """.trimIndent()
            } ?: "[\n| type: $type\n]"
        else ti?.run { if (line > -1) printInfo(provider, file, line, symbol, length).toString() else null } ?: "[(${type.operation})]"

    override fun equals(other: Any?): Boolean =
        this === other || (other is NodeInfoImpl && other.type == type && other.ti == ti)

    override fun hashCode(): Int =
        type.operation.hashCode() + (ti?.hashCode()?.let { it * 31 } ?: 0)

    companion object {
        fun printInfo(provider: Function<String, InputStream>, file: String, line: Int, symbol: Int, length: Int): StringBuilder =
            StringBuilder()
                .append('[').append(file).append(", ").append(line.inc()).append(", ").append(symbol.inc()).append("]\n")
                .append(readLine(line, provider.apply(file))).append('\n')
                .append(" ".repeat(symbol)).append('^').append("~".repeat(length.dec()))

        fun readLine(line: Int, stream: InputStream): StringBuilder {
            val buff = StringBuilder()
            val input = String(stream.readBytes())
            var lines = 0
            var i = 0
            while (i < input.length) {
                val it = input[i]
                if (it == '\n') {
                    lines++
                    if (lines > line)
                        break
                    else buff.clear()
                } else buff.append(it)
                i++
            }
            return buff
        }
    }
}