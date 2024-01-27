package ru.DmN.siberia.ast

import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.utils.indent

/**
 * Обработанная нода использования модулей.
 */
class NodeProcessedUse(
    info: INodeInfo,
    nodes: MutableList<Node>,
    names: MutableList<String>,
    /**
     * Обработанные ноды экспорта.
     */
    val exports: MutableList<NodeNodesList>,
    /**
     * Обработанные ноды используемых нод.
     */
    val processed: MutableList<Node>
) : NodeUse(info, nodes, names) {
    override fun copy(): NodeUse =
        NodeProcessedUse(info, copyNodes(), names, exports.map { it.copy() }.toMutableList(), processed.map { it.copy() }.toMutableList())

    override fun print(builder: StringBuilder, indent: Int, short: Boolean): StringBuilder {
        return builder.apply {
            indent(indent).append('[').append(info.type).append('\n')
                .indent(indent + 1).append("(modules =")
            names.forEach { builder.append(' ').append(it) }
            append(')')
            if (nodes.isNotEmpty()) {
                append('\n').indent(indent + 1).append("(NODES:\n")
                nodes.forEach { it.print(this, indent + 2, short).append('\n') }
                indent(indent + 1).append(')').append('\n').indent(indent)
            }
            if (!short && exports.isNotEmpty()) {
                if (nodes.isNotEmpty())
                    indent(1)
                else append('\n').indent(indent + 1)
                append("(EXPORTS:\n")
                exports.forEach { it.print(this, indent + 2, false).append('\n') }
                indent(indent + 1).append(')').append('\n').indent(indent)
            }
            append(']')
        }
    }
}