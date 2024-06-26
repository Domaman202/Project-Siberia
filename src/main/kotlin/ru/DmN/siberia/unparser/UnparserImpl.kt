package ru.DmN.siberia.unparser

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.unparser.ctx.UnparsingContext
import ru.DmN.siberia.unparsers.INodeUnparser
import ru.DmN.siberia.utils.exception.unparsingCatcher

/**
 * Стандартная реализация де-парсера.
 */
class UnparserImpl(
    override val mp: ModulesProvider,
    /**
     * Первичный размер буфера.
     */
    capacity: Int
) : Unparser() {
    override val out = StringBuilder(capacity)

    override fun unparse(node: Node, ctx: UnparsingContext, indent: Int) = unparsingCatcher(node) {
        get(ctx, node).unparse(node, this, ctx, indent)
    }

    @Suppress("UNCHECKED_CAST")
    override fun get(ctx: UnparsingContext, node: Node): INodeUnparser<Node> {
        val type = node.info.type
        ctx.loadedModules.forEach { it -> it.unparsers[type]?.let { return it as INodeUnparser<Node> } }
        throw RuntimeException("Unparser for \"$type\" not founded!")
    }
}