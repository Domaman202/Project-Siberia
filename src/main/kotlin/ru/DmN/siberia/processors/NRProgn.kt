package ru.DmN.siberia.processors

import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.ast.INodesList
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.utils.node.NodeTypes
import ru.DmN.siberia.utils.vtype.VirtualType

/**
 * Обработчик нод с под-нодами.
 */
object NRProgn : INodeProcessor<Node> {
    override fun calc(node: Node, processor: Processor, ctx: ProcessingContext): VirtualType? {
        node as INodesList
        return processor.calc(node.nodes.last(), ctx)
    }

    override fun process(node: Node, processor: Processor, ctx: ProcessingContext, valMode: Boolean): NodeNodesList {
        node as INodesList
        return NodeNodesList(
            node.info.withType(NodeTypes.PROGN_),
            if (node.nodes.isEmpty())
                ArrayList()
            else {
                val list =
                    node.nodes
                        .dropLast(1) // todo: drop last for sequence
                        .asSequence()
                        .map { processor.process(it, ctx, false) }
                        .filterNotNull()
                        .toMutableList()
                processor.process(node.nodes.last(), ctx, true)?.let { list += it }
                list
            }
        )
    }
}
