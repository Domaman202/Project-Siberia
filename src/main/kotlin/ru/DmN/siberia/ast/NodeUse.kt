package ru.DmN.siberia.ast

import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.utils.indent

/**
 * Нода использования модулей.
 */
open class NodeUse(
    info: INodeInfo,
    /**
     * Модули для использования.
     */
    val names: List<String>,
    nodes: MutableList<Node>
) : NodeNodesList(info, nodes) {

    override fun copy(): NodeUse =
        NodeUse(info, names, copyNodes())

    override fun print(builder: StringBuilder, indent: Int, short: Boolean): StringBuilder = builder.apply {
        indent(indent).append('[').append(info.type)
        if (names.isNotEmpty()) {
            append('\n')
            indent(indent + 1).append("(modules =")
            names.forEach { append(' ').append(it) }
            append(')')
        }
        printNodes(builder, indent, short).append(']')
    }
}