package ru.DmN.siberia.ast

import ru.DmN.siberia.utils.indent

/**
 * Интерфейс описывающий ноду, имеющую под-ноды.
 */
interface INodesList : Node {
    /**
     * Под-ноды.
     */
    val nodes: MutableList<Node>

    override fun copy(): INodesList =
        this

    override fun print(builder: StringBuilder, indent: Int): StringBuilder =
        printNodes(builder.indent(indent).append('[').append(info.type), indent).append(']')

    override fun printShort(builder: StringBuilder, indent: Int): StringBuilder =
        printNodesShort(builder.indent(indent).append('[').append(info.type), indent).append(']')

    /**
     * Печатает под-ноды в "builder".
     */
    fun printNodes(builder: StringBuilder, indent: Int): StringBuilder {
        if (nodes.isNotEmpty())
            builder.append('\n')
        nodes.forEach { it.print(builder, indent + 1).append('\n') }
        if (nodes.isNotEmpty())
            builder.indent(indent)
        return builder
    }

    /**
     * Печатает под-ноды в "builder" в кратком варианте.
     */
    fun printNodesShort(builder: StringBuilder, indent: Int): StringBuilder {
        if (nodes.isNotEmpty())
            builder.append('\n')
        nodes.forEach { it.printShort(builder, indent + 1).append('\n') }
        if (nodes.isNotEmpty())
            builder.indent(indent)
        return builder
    }
}