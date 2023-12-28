package ru.DmN.siberia.node

/**
 * Стандартная реализация INodeInfo.
 */
class NodeInfoImpl(override val type: INodeType, override val file: String?, override val line: Int?) : INodeInfo {
    override fun withType(type: INodeType): INodeInfo =
        NodeInfoImpl(type, file, line)

    override fun equals(other: Any?): Boolean =
        this === other || (other is NodeInfoImpl && other.type == type && other.file == file && other.line == line)

    override fun hashCode(): Int =
        (type.operation.hashCode() * 31 + (file?.hashCode() ?: 0)) * 31 + (line?.hashCode() ?: 0)
}