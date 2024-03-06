package ru.DmN.siberia.processors

import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.processor.ctx.ProcessingContext

object NRExport : INodeProcessor<Node> {
    override fun process(node: Node, processor: Processor, ctx: ProcessingContext, valMode: Boolean): Node? =
        null
}