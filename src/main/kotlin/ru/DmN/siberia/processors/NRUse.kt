package ru.DmN.siberia.processors

import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processors.NRUseCtx.injectModules
import ru.DmN.siberia.utils.node.NodeTypes.USE_

object NRUse : INodeProcessor<NodeUse> {
    override fun process(node: NodeUse, processor: Processor, ctx: ProcessingContext, valMode: Boolean): Node {
        val exports = ArrayList<NodeNodesList>()
        val processed = ArrayList<Node>()
        processor.mp.injectModules(node, processor, ctx, processed).forEach { it ->
            it.exports.forEach {
                exports += NRProgn.process(it.copy(), processor, ctx, false)
            }
        }
        return NodeProcessedUse(node.info.withType(USE_), ArrayList(), node.names, exports, processed)
    }
}