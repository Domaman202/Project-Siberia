package ru.DmN.siberia.unparsers

import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.unparser.Unparser
import ru.DmN.siberia.unparser.ctx.UnparsingContext
import ru.DmN.siberia.utils.operation

object NUUseCtxB : INodeUnparser<NodeProcessedUse> {
    override fun unparse(node: NodeProcessedUse, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        node.data.forEach { it.module.load(unparser, ctx) }
        unparser.out.apply {
            append('(').append(node.operation)
            node.data.forEach{ append(' ').append(it.module.name) }
            NUDefault.unparseNodes(node, unparser, ctx, indent)
            append(')')
        }
    }
}