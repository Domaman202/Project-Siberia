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

    override fun print(builder: StringBuilder, indent: Int): StringBuilder {
        builder.indent(indent).append('[').append(info.type)
        names.forEach { builder.append(' ').append(it) }
        return printNodes(builder, indent).append(']')
    }
}