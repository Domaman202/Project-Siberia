package ru.DmN.siberia.processors

import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.parsers.NPUseCtx.getModules
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
            processNodesList(node, processor, injectModules(node.names, processed, exports, processor, ctx), mode)
        }
        return NodeProcessedUse(node.info.withType(NodeTypes.USE_CTX_), node.nodes, node.names, exports, processed)
    }

    fun <T : Node> injectModules(names: MutableList<String>, processed: MutableList<Node>, exports: MutableList<T>, processor: Processor, ctx: ProcessingContext): ProcessingContext {
        val context = ctx.subCtx()
        injectModules(
            context.loadedModules,
            getModules(names.asSequence()),
            {
                it.load(processor, context, names)
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
                it.exports.forEach { nd -> exports += NRProgn.process(nd.copy(), processor, context, ValType.NO_VALUE) as T }
            }
        )
        return context
    }

    /**
     * Загружает модули в контекст процессинга из ноды.
     *
     * @param processed Список в который будут помещены обработанные ноды из модулей.
     */
    fun injectModules(node: NodeUse, processor: Processor, ctx: ProcessingContext, processed: MutableList<Node>): List<Module> =
        getModules(node.names).onEach { it ->
            if (it.load(processor, ctx, node.names)) {
                ctx.module = it
                it.nodes.forEach { it1 ->
                    processor.process(it1.copy(), ctx, ValType.NO_VALUE)?.let {
                        processed += it
                    }
                }
            }
        }

    /**
     * Загружает незагруженные модули.
     *
     * @param modules Список загруженных модулей.
     * @param uses Список необходимых модулей.
     * @param init Инициализация загруженных модулей.
     * @return Результат выполнения блока.
     */
    inline fun injectModules(modules: MutableList<Module>, uses: Sequence<Module>, noinline load: (Module) -> Unit, init: (Module) -> Unit) =
        uses.asSequence().filter { !modules.contains(it) }.onEach(load).forEach(init)
}