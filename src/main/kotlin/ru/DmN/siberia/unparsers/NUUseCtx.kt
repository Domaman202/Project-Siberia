package ru.DmN.siberia.unparsers

import ru.DmN.pht.module.utils.getOrLoadModule
import ru.DmN.siberia.unparser.Unparser
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.unparser.ctx.UnparsingContext
import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.utils.operation

object NUUseCtx : INodeUnparser<NodeUse> {
    override fun unparse(node: NodeUse, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        unparser.mp.loadModules(node.names, unparser, ctx)
        unparser.out.apply {
            append('(').append(node.operation)
            node.names.forEach{ append(' ').append(it) }
            NUDefault.unparseNodes(node, unparser, ctx, indent)
            append(')')
        }
    }

    /**
     * Загружает модули в контекст де-парсинга.
     *
     * @param names Имена модулей.
     */
    fun ModulesProvider.loadModules(names: List<String>, unparser: Unparser, ctx: UnparsingContext) {
        names.forEach { getOrLoadModule(it, ctx.platform).load(unparser, ctx) }
    }
}