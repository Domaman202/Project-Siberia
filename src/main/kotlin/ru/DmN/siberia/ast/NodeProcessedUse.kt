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

    override fun print(builder: StringBuilder, indent: Int, short: Boolean): StringBuilder {
        return builder.apply {
            indent(indent).append('[').append(info.type).append('\n')
                .indent(indent + 1).append("(modules = ")
            names.forEach { builder.append(' ').append(it) }
            var flag = false
            if (nodes.isNotEmpty()) {
                append('\n').indent(indent + 1).append("(NODES:\n")
                nodes.forEach { it.print(this, indent + 2, short).append('\n') }
                indent(indent + 1).append(')').append('\n').indent(indent)
                flag = true
            }
            if (!short) {
                if (exports.isNotEmpty()) {
                    if (flag)
                        indent(1)
                    else append('\n').indent(indent + 1)
                    append("(EXPORTS:\n")
                    exports.forEach { it.print(this, indent + 2, false).append('\n') }
                    indent(indent + 1).append(')').append('\n').indent(indent)
                    flag = true
                }
                if (processed.isNotEmpty()) {
                    if (flag)
                        indent(1)
                    else append('\n').indent(indent + 1)
                    append("(PROCESSED:\n")
                    processed.forEach { it.print(this, indent + 2, false).append('\n') }
                    indent(indent + 1).append(')').append('\n').indent(indent)
                }
            }
            append(']')
        }
    }
}