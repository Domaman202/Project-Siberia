package ru.DmN.siberia.unparsers

import ru.DmN.siberia.ast.NodeProcessedLoad
import ru.DmN.siberia.unparser.Unparser
import ru.DmN.siberia.unparser.ctx.UnparsingContext
import ru.DmN.siberia.utils.operation

object NULoadB : INodeUnparser<NodeProcessedLoad> {
    override fun unparse(node: NodeProcessedLoad, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        node.modules.forEach { it.load(unparser, ctx) }
        unparser.out.apply {
            append('(').append(node.operation)
            node.modules.forEach{ append(' ').append(it.name) }
            NUDefault.unparseNodes(node, unparser, ctx, indent)
            append(')')
        }
    }
}