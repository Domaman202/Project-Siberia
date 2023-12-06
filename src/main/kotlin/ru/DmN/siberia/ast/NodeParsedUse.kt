package ru.DmN.siberia.ast

import ru.DmN.siberia.lexer.Token

/**
 * Обработанная нода использования модулей.
 */
open class NodeParsedUse(
    tkOperation: Token,
    names: List<String>,
    nodes: MutableList<Node>
) : NodeUse(tkOperation, names, nodes)