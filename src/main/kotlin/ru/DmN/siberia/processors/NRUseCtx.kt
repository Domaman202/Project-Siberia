package ru.DmN.siberia.processors

import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ProcessingStage
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.module
import ru.DmN.siberia.processor.utils.processNodesList
import ru.DmN.siberia.utils.Module

object NRUseCtx : INodeProcessor<NodeUse> {
    override fun process(node: NodeUse, processor: Processor, ctx: ProcessingContext, mode: ValType): Node {
        val exports = ArrayList<NodeNodesList>()
        val processed = ArrayList<Node>()
        processor.stageManager.pushTask(ProcessingStage.MODULE_POST_INIT) {
            val context = ctx.subCtx()
            injectModules(node, processor, context, ValType.NO_VALUE, processed).forEach { it ->
                context.module = ctx.module
                it.exports.forEach {
                    exports += NRProgn.process(it.copy(), processor, context, ValType.NO_VALUE)
                }
            }
            processNodesList(node, processor, context, mode)
        }
        return NodeProcessedUse(node.info.withType(NodeTypes.USE_CTX_), node.names, node.nodes, exports, processed)
    }

    /**
     * Загружает модули в контекст процессинга из ноды.
     *
     * @param processed Список в который будут помещены обработанные ноды из модулей.
     */
    fun injectModules(node: NodeUse, processor: Processor, ctx: ProcessingContext, mode: ValType, processed: MutableList<Node>): List<Module> =
        node.names
            .asSequence()
            .map { Module.getOrThrow(it) }
            .onEachIndexed { i, it ->
                if (it.load(processor, ctx, if (i + 1 < node.names.size) ValType.NO_VALUE else mode)) {
                    ctx.module = it
                    it.nodes.forEach { it1 ->
                        processor.process(it1.copy(), ctx, ValType.NO_VALUE)?.let {
                            processed += it
                        }
                    }
                }
            }
            .toList()
}