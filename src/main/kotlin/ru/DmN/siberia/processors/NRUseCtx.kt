package ru.DmN.siberia.processors

import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.*
import ru.DmN.siberia.utils.Module

object NRUseCtx : INodeProcessor<NodeUse> {
    override fun process(node: NodeUse, processor: Processor, ctx: ProcessingContext, mode: ValType): Node {
        if (node.names.isEmpty())
            return NRProgn.process(nodeProgn(node.info, node.nodes), processor, ctx, mode)
        val exports = ArrayList<NodeNodesList>()
        val processed = ArrayList<Node>()
        processor.stageManager.pushTask(ProcessingStage.MODULE_POST_INIT) {
            val context = ctx.subCtx()
            injectModules(
                context.loadedModules,
                node.names.map(Module::getOrThrow),
                {
                    it.load(processor, context, ValType.NO_VALUE)
                    val tmpContext = context.subCtx()
                    tmpContext.module = it
                    it.nodes.forEach { nd ->
                        processor.process(nd.copy(), tmpContext, ValType.NO_VALUE)?.let { pn ->
                            processed += pn
                        }
                    }
                },
                {
                    context.module = ctx.module
                    it.exports.forEach { nd -> exports += NRProgn.process(nd.copy(), processor, context, ValType.NO_VALUE) }
                }
            )
            processNodesList(node, processor, context, mode)
        }
        return NodeProcessedUse(node.info.withType(NodeTypes.USE_CTX_), node.names, node.nodes, exports, processed)
    }

    /**
     * Загружает модули в контекст процессинга из ноды.
     *
     * @param processed Список в который будут помещены обработанные ноды из модулей.
     */
    fun injectModules(node: NodeUse, processor: Processor, ctx: ProcessingContext, processed: MutableList<Node>): List<Module> =
        node.names
            .asSequence()
            .map { Module.getOrThrow(it) }
            .onEach { it ->
                if (it.load(processor, ctx, ValType.NO_VALUE)) {
                    ctx.module = it
                    it.nodes.forEach { it1 ->
                        processor.process(it1.copy(), ctx, ValType.NO_VALUE)?.let {
                            processed += it
                        }
                    }
                }
            }
            .toList()

    /**
     * Загружает незагруженные модули.
     *
     * @param modules Список загруженных модулей.
     * @param uses Список необходимых модулей.
     * @param init Инициализация загруженных модулей.
     * @return Результат выполнения блока.
     */
    private inline fun injectModules(modules: MutableList<Module>, uses: List<Module>, load: (Module) -> Unit, init: (Module) -> Unit) =
        uses.filter { !modules.contains(it) }.onEach(load).forEach(init)
}