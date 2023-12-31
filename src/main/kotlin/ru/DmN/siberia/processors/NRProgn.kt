package ru.DmN.siberia.processors

import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.utils.VirtualType

/**
 * Обработчик нод с под-нодами.
 */
object NRProgn : INodeProcessor<NodeNodesList> {
    override fun calc(node: NodeNodesList, processor: Processor, ctx: ProcessingContext): VirtualType? =
        processor.calc(node.nodes.last(), ctx)

    override fun process(node: NodeNodesList, processor: Processor, ctx: ProcessingContext, mode: ValType): NodeNodesList =
        NodeNodesList(
            node.info.withType(NodeTypes.PROGN_),
            if (node.nodes.isEmpty())
                ArrayList()
            else {
                val list =
                    node.nodes
                        .dropLast(1) // todo: drop last for sequence
                        .asSequence()
                        .map { processor.process(it, ctx, ValType.NO_VALUE) }
                        .filterNotNull()
                        .toMutableList()
                processor.process(node.nodes.last(), ctx, ValType.VALUE)?.let { list += it }
                list
            }
        )
}
