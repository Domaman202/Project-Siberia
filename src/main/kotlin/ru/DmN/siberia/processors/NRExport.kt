package ru.DmN.siberia.processors

import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType

object NRExport : INodeProcessor<Node> {
    override fun process(node: Node, processor: Processor, ctx: ProcessingContext, mode: ValType): Node? =
        null
}