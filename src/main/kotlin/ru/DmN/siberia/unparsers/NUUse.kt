package ru.DmN.siberia.unparsers

import ru.DmN.siberia.Unparser
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.utils.operation

object NUUse : INodeUnparser<NodeUse> {
    override fun unparse(node: NodeUse, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        NUUseCtx.loadModules(node.names, unparser, ctx)
        unparser.out.apply {
            append('(').append(node.operation).append(' ')
            node.names.forEach(this::append)
            append(')')
        }
    }
}