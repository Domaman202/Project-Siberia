package ru.DmN.siberia.utils.exception

import ru.DmN.siberia.utils.node.INodeInfo
import ru.DmN.siberia.utils.node.NodeInfoImpl
import java.io.InputStream
import java.util.function.Function

open class NodeInfoException(prev: BaseException?, val info: INodeInfo) : BaseException(prev) {
    override fun print(sb: StringBuilder, provider: Function<String, InputStream>?): StringBuilder {
        // [Experimental]
//        if (prev is NodeInfoException) {
//            val info = info.ti!!
//            val line = info.line
//            val file = info.file
//            val symbol = mutableListOf(info.symbol)
//            val length = mutableListOf(info.length)
//            //
//            var last: BaseException? = prev
//            while (last is NodeInfoException) {
//                val ti = last.info.ti!!
//                if (ti.file != file || ti.line != line)
//                    break
//                //
//                if (symbol.last() != ti.symbol) {
//                    symbol += ti.symbol
//                    length += ti.length
//                }
//                //
//                last = last.prev
//            }
//            //
//            if (symbol.size > 1) {
//                var offset = 0
//                val buff =
//                    StringBuilder()
//                        .append('[').append(file).append(", ").append(line.inc()).append("]\n")
//                        .append(NodeInfoImpl.readLine(line, provider!!.apply(file))).append('\n')
//                length.forEachIndexed { i, it ->
//                    buff.append(" ".repeat(symbol[i] - offset)).append('^').append("~".repeat(it.dec()))
//                    offset += it.inc()
//                }
//                return sb.append(buff)
//            }
//        }
        // [Stable]
        sb.append(info.print(provider))
        var it = prev
        while (true) {
            if (it is NodeInfoException && it.info.ti!!.let { a -> info.ti!!.let { b -> a.symbol == b.symbol && a.file == b.file } }) {
                it = it.prev
                continue
            }
            it?.print(sb.append('\n'), provider)
            break
        }
        return sb
        // [Stable]
//        sb.append(info.print(provider))
//        prev?.print(sb.append('\n'), provider)
//        return sb
    }
}