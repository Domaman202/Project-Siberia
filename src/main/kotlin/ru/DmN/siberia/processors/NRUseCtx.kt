package ru.DmN.siberia.processors

import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.parsers.NPUseCtx.getModules
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ProcessingStage.MODULE_POST_INIT
import ru.DmN.siberia.processor.utils.module
import ru.DmN.siberia.processor.utils.nodeProgn
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.processor.utils.processNodesList
import ru.DmN.siberia.utils.node.NodeTypes.USE_CTX_

object NRUseCtx : INodeProcessor<NodeUse> {
    override fun process(node: NodeUse, processor: Processor, ctx: ProcessingContext, valMode: Boolean): Node {
        if (node.names.isEmpty())
            return NRProgn.process(nodeProgn(node.info, node.nodes), processor, ctx, valMode)
        val exports = ArrayList<NodeNodesList>()
        val processed = ArrayList<Node>()
        processor.stageManager.pushTask(MODULE_POST_INIT) {
            processNodesList(node, processor, processor.mp.injectModules(node.names, processed, exports, processor, ctx), valMode)
        }
        return NodeProcessedUse(node.info.withType(USE_CTX_), node.nodes, node.names, exports, processed)
    }

    fun <T : Node> ModulesProvider.injectModules(names: MutableList<String>, processed: MutableList<Node>, exports: MutableList<T>, processor: Processor, ctx: ProcessingContext): ProcessingContext {
        val context = ctx.subCtx()
        injectModules(
            context.loadedModules,
            getModules(names.asSequence(), ctx.platform),
            {
                it.load(processor, context, names)
                val tmpContext = context.subCtx()
                tmpContext.module = it
                it.nodes.forEach { nd ->
                    processor.process(nd.copy(), tmpContext, false)?.let { pn ->
                        processed += pn
                    }
                }
            },
            {
                context.module = ctx.module
                it.exports.forEach { nd -> exports += NRProgn.process(nd.copy(), processor, context, false) as T }
            }
        )
        return context
    }

    /**
     * Загружает модули в контекст процессинга из ноды.
     *
     * @param processed Список в который будут помещены обработанные ноды из модулей.
     */
    fun ModulesProvider.injectModules(node: NodeUse, processor: Processor, ctx: ProcessingContext, processed: MutableList<Node>): List<Module> =
        getModules(node.names, ctx.platform).onEach { it ->
            if (it.load(processor, ctx, node.names)) {
                ctx.module = it
                it.nodes.forEach { it1 ->
                    processor.process(it1.copy(), ctx, false)?.let {
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
        uses.asSequence().filter { !modules.contains(it) }.onEach(load).forEach(init) // todo: asSequence?
}