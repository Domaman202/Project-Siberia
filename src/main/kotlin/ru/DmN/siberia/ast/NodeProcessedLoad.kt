package ru.DmN.siberia.ast

import ru.DmN.pht.module.utils.Module
import ru.DmN.siberia.utils.indent
import ru.DmN.siberia.utils.node.INodeInfo

/**
 * Обработанная нода загрузки модулей.
 */
class NodeProcessedLoad(
    info: INodeInfo,
    nodes: MutableList<Node>,
    val modules: List<Module>
) : NodeNodesList(info, nodes) {
    override fun copy(): NodeProcessedLoad =
        NodeProcessedLoad(info, copyNodes(), modules)

    override fun print(builder: StringBuilder, indent: Int): StringBuilder = builder.apply {
        indent(indent).append('[').append(info.type).append('\n')
        indent(indent + 1).append("(modules =")
        modules.forEach { builder.append(' ').append(it.name) }
        append(')')
        if (nodes.isNotEmpty()) {
            append('\n').indent(indent + 1).append("(NODES:\n")
            nodes.forEach { it.print(this, indent + 2).append('\n') }
            indent(indent + 1).append(')').append('\n').indent(indent)
        }
        append(']')
    }
}