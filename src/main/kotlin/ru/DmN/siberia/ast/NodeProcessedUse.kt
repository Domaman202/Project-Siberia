package ru.DmN.siberia.ast

import ru.DmN.siberia.lexer.Token

/**
 * Обработанная нода использования модулей.
 */
class NodeProcessedUse(
    token: Token,
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
) : NodeUse(token, names, nodes) {
    override fun copy(): NodeUse =
        NodeProcessedUse(token, names, copyNodes(), exports.map { it.copy() }.toMutableList(), processed.map { it.copy() }.toMutableList())
}