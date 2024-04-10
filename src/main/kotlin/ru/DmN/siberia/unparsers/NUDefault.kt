package ru.DmN.siberia.unparsers

import ru.DmN.siberia.unparser.Unparser
import ru.DmN.siberia.ast.INodesList
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.unparser.ctx.UnparsingContext
import ru.DmN.siberia.utils.operation

/**
 * Де-парсер для нод с под-нодами.
 */
object NUDefault : INodeUnparser<Node> {
    override fun unparse(node: Node, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        node as INodesList
        unparser.out.run {
            append('(').append(node.operation)
            unparseNodes(node, unparser, ctx, indent)
            append(')')
        }
    }

    /**
     * Де-парсит под-ноды ноды.
     */
    fun unparseNodes(node: INodesList, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        node.nodes.forEach { n ->
            unparser.out.append('\n').append("\t".repeat(indent + 1))
            unparser.unparse(n, ctx, indent + 1)
        }
    }
}