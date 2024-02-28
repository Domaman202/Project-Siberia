package ru.DmN.siberia.ast

import ru.DmN.siberia.utils.node.INodeInfo
import ru.DmN.siberia.utils.indent

/**
 * Нода с под-нодами.
 */
open class NodeNodesList(info: INodeInfo, override val nodes: MutableList<Node> = mutableListOf()) : BaseNode(info), INodesList {
    override fun copy(): NodeNodesList =
        NodeNodesList(info, copyNodes())

    /**
     * Копирует под-ноды
     */
    fun copyNodes(): MutableList<Node> =
        nodes.map { it.copy() }.toMutableList()

    override fun print(builder: StringBuilder, indent: Int, short: Boolean): StringBuilder =
        printNodes(builder.indent(indent).append('[').append(info.type), indent, short).append(']')

    /**
     * Печатает под-ноды в "builder".
     */
    fun printNodes(builder: StringBuilder, indent: Int, short: Boolean): StringBuilder {
        if (nodes.isNotEmpty())
            builder.append('\n')
        nodes.forEach { it.print(builder, indent + 1, short).append('\n') }
        if (nodes.isNotEmpty())
            builder.indent(indent)
        return builder
    }
}