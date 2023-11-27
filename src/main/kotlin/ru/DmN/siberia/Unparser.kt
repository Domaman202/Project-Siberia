package ru.DmN.siberia

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.unparsers.INodeUnparser
import ru.DmN.siberia.utils.getRegex
import ru.DmN.siberia.utils.text

/**
 * Де-парсер
 */
class Unparser {
    /**
     * Выход
     */
    val out = StringBuilder()

    /**
     * Де-парсит ноду.
     */
    fun unparse(node: Node, ctx: UnparsingContext, indent: Int) =
        get(ctx, node).unparse(node, this, ctx, indent)

    /**
     * Возвращает де-парсер нод.
     */
    fun get(ctx: UnparsingContext, node: Node): INodeUnparser<Node> {
        val name = node.text
        ctx.loadedModules.forEach { it -> it.unparsers.getRegex(name)?.let { return it as INodeUnparser<Node> } }
        throw RuntimeException()
    }
}