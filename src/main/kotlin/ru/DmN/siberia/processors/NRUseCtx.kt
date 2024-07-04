package ru.DmN.siberia.processors

import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.ast.NodeProcessedUse.ProcessedData
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.parsers.NPUseCtx.getModules
import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.*
import ru.DmN.siberia.processor.utils.ProcessingStage.MODULE_POST_INIT
import ru.DmN.siberia.utils.exception.pushTask
import ru.DmN.siberia.utils.node.NodeTypes.USE_CTX_

object NRUseCtx : INodeProcessor<NodeUse> { // todo: calc?
    override fun process(node: NodeUse, processor: Processor, ctx: ProcessingContext, valMode: Boolean): Node {
        if (node.names.isEmpty())
            return NRProgn.process(nodeProgn(node.info, node.nodes), processor, ctx, valMode)
        val data = ArrayList<ProcessedData>()
        processor.pushTask(MODULE_POST_INIT, node) {
            processNodesList(node, processor, processor.mp.injectModules(node.names, data, processor, ctx), valMode)
        }
        return NodeProcessedUse(node.info.withType(USE_CTX_), node.nodes, data)
    }

    fun ModulesProvider.injectModules(names: MutableList<String>, data: MutableList<ProcessedData>, processor: Processor, ctx: ProcessingContext): ProcessingContext {
        val context = ctx.subCtx()
        val platform = ctx.platform
        injectModules(
            context.loadedModules,
            getModules(names.asSequence(), platform).onEach { ProcessedData(it).apply { data += this } }.toList(),
            { module ->
                module.load(processor, context, names)
                val moduleData = processor.mp.getModuleData(module.name)
                val processed = moduleData.processedNodes[platform]
                if (processed == null) {
                    val tmpContext = ProcessingContext.base().with(platform)
                    tmpContext.module = module
                    val list = data.find { it.module == module }!!.processed
                    moduleData.processedNodes[platform] = list
                    module.nodes.forEach { nd ->
                        processor.process(nd.copy(), tmpContext, false)?.let(list::add)
                    }
                } else data.find { it.module == module }!!.processed = processed
            },
            { module ->
                context.module = ctx.module
                val list = data.find { it.module == module }!!.exports
                module.exports.forEach { nd ->
                    list += NRProgn.process(nd.copy(), processor, context, false)
                }
            }
        )
        return context
    }

    /**
     * Загружает незагруженные модули.
     *
     * @param modules Список загруженных модулей.
     * @param uses Список необходимых модулей.
     * @param init Инициализация загруженных модулей.
     * @return Результат выполнения блока.
     */
    inline fun injectModules(modules: MutableList<Module>, uses: List<Module>, noinline load: (Module) -> Unit, init: (Module) -> Unit) =
        uses.filter { !modules.contains(it) }.onEach(load).forEach(init)
}