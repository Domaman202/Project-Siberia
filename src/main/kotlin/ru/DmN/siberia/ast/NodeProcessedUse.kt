package ru.DmN.siberia.ast

import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.utils.indent

/**
 * Обработанная нода использования модулей.
 */
class NodeProcessedUse(
    info: INodeInfo,
    names: List<String>,
    nodes: MutableList<Node>,
    /**
     * Обработанные ноды экспорта.
     */
    val exports: MutableList<NodeNodesList>,
    /**
     * Обработанные ноды используемых нод.
     */
    val processed: MutableList<Node>
) : NodeUse(info, names, nodes) {
    override fun copy(): NodeUse =
        NodeProcessedUse(info, names, copyNodes(), exports.map { it.copy() }.toMutableList(), processed.map { it.copy() }.toMutableList())

    override fun print(builder: StringBuilder, indent: Int): StringBuilder {
        builder.indent(indent).append('[').append(info.type)
        names.forEach { builder.append(' ').append(it) }
        printNodes(builder, indent)
        if (processed.isNotEmpty())
            builder.append('\n')
        processed.forEach { it.print(builder, indent + 1).append('\n') }
        if (processed.isNotEmpty())
            builder.indent(indent)
        return builder.append(']')
    }
}