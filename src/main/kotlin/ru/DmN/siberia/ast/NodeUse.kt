package ru.DmN.siberia.ast

import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.utils.indent
import ru.DmN.siberia.utils.text

/**
 * Нода использования модулей.
 */
open class NodeUse(
    token: Token,
    /**
     * Модули для использования.
     */
    val names: List<String>,
    nodes: MutableList<Node>
) : NodeNodesList(token, nodes) {

    override fun copy(): NodeUse =
        NodeUse(token, names, copyNodes())

    override fun print(builder: StringBuilder, indent: Int): StringBuilder {
        builder.indent(indent).append('[').append(text)
        names.forEach { builder.append(' ').append(it) }
        return printNodes(builder, indent).append(']')
    }
}