package ru.DmN.siberia.unparsers

import ru.DmN.siberia.Unparser
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.utils.operation

/**
 * Де-парсер для нод с под-нодами.
 */
object NUDefault : INodeUnparser<NodeNodesList> {
    override fun unparse(node: NodeNodesList, unparser: Unparser, ctx: UnparsingContext, indent: Int, line: Boolean) {
        unparser.out.run {
            append('(').append(node.operation)
            if (node.nodes.isNotEmpty()) {
                append(' ')
                if (line) {
                    append('\n').append("\t".repeat(indent + 1))
                    unparseNodes(node, unparser, ctx, indent + 1)
                } else unparseNodes(node, unparser, ctx, indent)
            }
            append(')')
        }
    }

    /**
     * Де-парсит под-ноды ноды.
     */
    fun unparseNodes(node: NodeNodesList, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        node.nodes.forEach { n ->
            unparser.out.append('\n').append("\t".repeat(indent + 1))
            unparser.unparse(n, ctx, indent + 1, false)
        }
    }
}