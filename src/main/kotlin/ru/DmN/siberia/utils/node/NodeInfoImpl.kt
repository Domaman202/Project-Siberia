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
        else ti!!.run {
            val input = String(provider.apply(file).readBytes())
            val sb = StringBuilder()
            var lines = 0
            var j = 0
            while (j < input.length) {
                val it = input[j]
                if (it == '\n') {
                    lines++
                    if (lines > line)
                        break
                    else sb.clear()
                } else sb.append(it)
                j++
            }
            //
            sb.append('\n').append(" ".repeat(symbol)).append('^').append("~".repeat(length - 1)).toString()
        }

    override fun equals(other: Any?): Boolean =
        this === other || (other is NodeInfoImpl && other.type == type && other.ti == ti)

    override fun hashCode(): Int =
        type.operation.hashCode() + (ti?.hashCode()?.let { it * 31 } ?: 0)
}