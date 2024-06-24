package ru.DmN.siberia.ast

import ru.DmN.pht.module.utils.Module
import ru.DmN.siberia.utils.indent
import ru.DmN.siberia.utils.node.INodeInfo

/**
 * Обработанная нода использования модулей.
 */
class NodeProcessedUse(
    info: INodeInfo,
    nodes: MutableList<Node>,
    val data: List<ProcessedData>
) : NodeNodesList(info, nodes) {
    override fun copy(): NodeProcessedUse =
        NodeProcessedUse(info, copyNodes(), data)

    override fun print(builder: StringBuilder, indent: Int, short: Boolean): StringBuilder {
        return builder.apply {
            indent(indent).append('[').append(info.type).append('\n')
                .indent(indent + 1).append("(modules =")
            data.forEach { builder.append(' ').append(it.module.name) }
            append(')')
            if (nodes.isNotEmpty()) {
                append('\n').indent(indent + 1).append("(NODES:\n")
                nodes.forEach { it.print(this, indent + 2, short).append('\n') }
                indent(indent + 1).append(')').append('\n').indent(indent)
            }
//            if (!short && exports.isNotEmpty()) {
//                if (nodes.isNotEmpty())
//                    indent(1)
//                else append('\n').indent(indent + 1)
//                append("(EXPORTS:\n")
//                exports.forEach { it.print(this, indent + 2, false).append('\n') }
//                indent(indent + 1).append(')').append('\n').indent(indent)
//            }
            append(']')
        }
    }

    /**
     * Результат обработки модуля.
     */
    data class ProcessedData(
        /**
         * Модуль.
         */
        val module: Module,

        /**
         * Обработанные ноды модуля.
         */
        val processed: MutableList<Node> = ArrayList(),

        /**
         * Обработанные ноды экспорта модуля.
         */
        val exports: MutableList<Node> = ArrayList()
    )
}