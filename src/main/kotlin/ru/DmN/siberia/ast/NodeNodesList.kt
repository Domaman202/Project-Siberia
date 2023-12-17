package ru.DmN.siberia.ast

import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.utils.indent

/**
 * Нода с под-нодами.
 */
open class NodeNodesList(info: INodeInfo, override val nodes: MutableList<Node> = mutableListOf()) : Node(info), INodesList {
    override fun copy(): NodeNodesList =
        NodeNodesList(info, copyNodes())

    /**
     * Копирует под-ноды
     */
    fun copyNodes(): MutableList<Node> =
        nodes.map { it.copy() }.toMutableList()

    override fun print(builder: StringBuilder, indent: Int): StringBuilder =
        printNodes(builder.indent(indent).append('[').append(info.type), indent).append(']')

    fun printNodes(builder: StringBuilder, indent: Int): StringBuilder {
        if (nodes.isNotEmpty())
            builder.append('\n')
        nodes.forEach { it.print(builder, indent + 1).append('\n') }
        if (nodes.isNotEmpty())
            builder.indent(indent)
        return builder
    }
}