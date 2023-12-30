package ru.DmN.siberia.processors

import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processors.NRUseCtx.injectModules

object NRUse : INodeProcessor<NodeUse> {
    override fun process(node: NodeUse, processor: Processor, ctx: ProcessingContext, mode: ValType): Node {
        val exports = ArrayList<NodeNodesList>()
        val processed = ArrayList<Node>()
        injectModules(node, processor, ctx, processed).forEach { it ->
            it.exports.forEach {
                exports += NRProgn.process(it.copy(), processor, ctx, ValType.NO_VALUE)
            }
        }
        return NodeProcessedUse(node.info.withType(NodeTypes.USE_), node.names, ArrayList(), exports, processed)
    }
}