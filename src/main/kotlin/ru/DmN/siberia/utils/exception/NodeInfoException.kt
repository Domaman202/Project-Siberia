package ru.DmN.siberia.utils.exception

import ru.DmN.siberia.utils.node.INodeInfo
import ru.DmN.siberia.utils.node.NodeInfoImpl
import java.io.InputStream
import java.util.function.Function

open class NodeInfoException(prev: BaseException?, val info: INodeInfo) : BaseException(prev) {
    override fun print(sb: StringBuilder, provider: Function<String, InputStream>?): StringBuilder {
        if (prev is NodeInfoException) {
            val info = info.ti!!
            val line = info.line
            val file = info.file
            val symbol = mutableListOf(info.symbol)
            val length = mutableListOf(info.length)
            var last: BaseException? = prev
            //
            while (last is NodeInfoException) {
                val ti = last.info.ti!!
                if (ti.file != file || ti.line != line)
                    break
                symbol += ti.symbol
                length += ti.length
                last = last.prev
            }
            //
            if (symbol.size > 1) {
                var offset = 0
                val buff =
                    StringBuilder()
                        .append('[').append(file).append(", ").append(line.inc()).append("]\n")
                        .append(NodeInfoImpl.readLine(line, provider!!.apply(file))).append('\n')
                length.forEachIndexed { i, it ->
                    buff.append(" ".repeat(symbol[i] - offset)).append('^').append("~".repeat(it.dec()))
                    offset += it.inc()
                }
                return sb.append(buff)
            }
        }
        //
        sb.append(info.print(provider))
        prev?.print(sb.append('\n'), provider)
        return sb
    }
}