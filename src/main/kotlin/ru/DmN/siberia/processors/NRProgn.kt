package ru.DmN.siberia.processors

import ru.DmN.siberia.ast.INodesList
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.utils.node.NodeTypes.PROGN_
import ru.DmN.siberia.utils.vtype.VirtualType

/**
 * Обработчик нод с под-нодами.
 */
object NRProgn : INodeProcessor<INodesList> {
    override fun calc(node: INodesList, processor: Processor, ctx: ProcessingContext): VirtualType? =
        node.nodes.let { if (it.isEmpty()) null else processor.calc(it.last(), ctx) }

    override fun process(node: INodesList, processor: Processor, ctx: ProcessingContext, valMode: Boolean): NodeNodesList =
        NodeNodesList(
            node.info.withType(PROGN_),
            if (node.nodes.isEmpty())
                ArrayList()
            else {
                val list = ArrayList<Node>()
                val nodes = node.nodes
                for (i in 0 until nodes.size - 1)
                    processor.process(nodes[i], ctx, false).let { if (it != null) list += it }
                processor.process(node.nodes.last(), ctx, true)?.let { list += it }
                list
            }
        )
}
