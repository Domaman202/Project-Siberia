package ru.DmN.siberia.unparsers

import ru.DmN.siberia.unparser.Unparser
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.unparsers.NUUseCtx.loadModules
import ru.DmN.siberia.utils.operation

object NUUse : INodeUnparser<NodeUse> {
    override fun unparse(node: NodeUse, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        unparser.mp.loadModules(node.names, unparser, ctx)
        unparser.out.apply {
            append('(').append(node.operation).append(' ')
            node.names.forEach(this::append)
            append(')')
        }
    }
}