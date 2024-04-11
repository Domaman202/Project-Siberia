package ru.DmN.siberia.utils.exception

import ru.DmN.siberia.utils.node.INodeInfo
import java.io.InputStream
import java.util.function.Function

open class NodeInfoException(prev: BaseException?, val info: INodeInfo) : BaseException(prev) {
    override fun print(sb: StringBuilder, provider: Function<String, InputStream>?): StringBuilder {
        sb.append(info.print(provider))
        prev?.print(sb.append('\n'), provider)
        return sb
    }
}