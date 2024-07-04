package ru.DmN.siberia.processors

import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.module.utils.getOrLoadModule
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeProcessedLoad
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ProcessingStage.MODULE_POST_INIT
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.processor.utils.processNodesList
import ru.DmN.siberia.utils.exception.pushTask
import ru.DmN.siberia.utils.node.NodeTypes.LOAD_CTX_

object NRLoadCtx : INodeProcessor<NodeUse> { // todo: calc?
    override fun process(node: NodeUse, processor: Processor, ctx: ProcessingContext, valMode: Boolean): NodeNodesList {
        val modules = ArrayList<Module>()
        processor.pushTask(MODULE_POST_INIT, node) {
            val context = ctx.subCtx()
            val loadedModules = context.loadedModules
            val platform = context.platform
            val names = node.names
            var i = 0
            while (i < names.size) {
                val module = processor.mp.getOrLoadModule(names[i], platform)
                modules += module
                if (!loadedModules.contains(module)) {
                    loadedModules += module
                    module.load(processor, ctx, names)
                }
                i++
            }
            processNodesList(node, processor, context, valMode)
        }
        return NodeProcessedLoad(node.info.withType(LOAD_CTX_), node.nodes, modules)
    }
}
