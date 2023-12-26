package ru.DmN.siberia.node

/**
 * Стандартная реализация INodeInfo.
 */
class NodeInfoImpl(override val type: INodeType, override val file: String?, override val line: Int?) : INodeInfo {
    override fun withType(type: INodeType): INodeInfo =
        NodeInfoImpl(type, file, line)
}