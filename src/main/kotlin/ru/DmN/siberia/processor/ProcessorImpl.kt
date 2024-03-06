package ru.DmN.siberia.processor

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ProcessingStage
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.utils.ctx.IContextKey
import ru.DmN.siberia.utils.stage.StageManager
import ru.DmN.siberia.utils.stage.StupidStageManager
import ru.DmN.siberia.utils.vtype.TypesProvider
import ru.DmN.siberia.utils.vtype.VirtualType

/**
 * Стандартная реализация обработчика.
 */
class ProcessorImpl(override val mp: ModulesProvider, override val tp: TypesProvider) : Processor() {
    override val stageManager: StageManager<ProcessingStage> = StupidStageManager.of(ProcessingStage.UNKNOWN)
    override val contexts: MutableMap<IContextKey, Any?> = HashMap()

    override fun calc(node: Node, ctx: ProcessingContext): VirtualType? =
        get(node, ctx).calc(node, this, ctx)

    override fun process(node: Node, ctx: ProcessingContext, valMode: Boolean): Node? =
        if (node.info.type.processable)
            get(node, ctx).process(node, this, ctx, valMode)
        else node

    override fun get(node: Node, ctx: ProcessingContext): INodeProcessor<Node> {
        val type = node.info.type
        ctx.loadedModules.forEach { it -> it.processors[type]?.let { return it as INodeProcessor<Node> } }
        throw RuntimeException("Processor for \"$type\" not founded!")
    }
}