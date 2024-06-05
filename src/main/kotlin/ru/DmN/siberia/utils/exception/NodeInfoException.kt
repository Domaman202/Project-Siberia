package ru.DmN.siberia.utils.exception

import ru.DmN.siberia.utils.node.INodeInfo
import java.io.InputStream
import java.util.function.Function

open class NodeInfoException(prev: BaseException?, val info: INodeInfo) : BaseException(prev) {
    override fun print(sb: StringBuilder, provider: Function<String, InputStream>?): StringBuilder {
        sb.append(info.print(provider))
        var it = prev
        while (true) {
            if (it is NodeInfoException && it.info.ti?.let { a -> info.ti?.let { b -> a.symbol == b.symbol && a.file == b.file } } == true) {
                it = it.prev
                continue
            }
            it?.print(sb.append('\n'), provider)
            break
        }
        return sb
    }
}