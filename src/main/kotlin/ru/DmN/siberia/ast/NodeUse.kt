package ru.DmN.siberia.ast

import ru.DmN.siberia.utils.indent
import ru.DmN.siberia.utils.node.INodeInfo

/**
 * Нода использования модулей.
 */
class NodeUse(
    info: INodeInfo,
    nodes: MutableList<Node>,
    /**
     * Модули для использования.
     */
    val names: MutableList<String>
) : NodeNodesList(info, nodes) {

    override fun copy(): NodeUse =
        NodeUse(info, copyNodes(), names)

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