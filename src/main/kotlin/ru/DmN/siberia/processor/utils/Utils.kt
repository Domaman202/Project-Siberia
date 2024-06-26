package ru.DmN.siberia.processor.utils

import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.INodesList
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.processor.ctx.ContextKeys.*
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.utils.IPlatform
import ru.DmN.siberia.utils.ctx.IContextCollection
import ru.DmN.siberia.utils.node.INodeInfo
import ru.DmN.siberia.utils.node.NodeTypes
import ru.DmN.siberia.utils.vtype.TypesProvider

fun processNodesList(node: INodesList, processor: Processor, ctx: ProcessingContext, valMode: Boolean) {
    if (node.nodes.isNotEmpty()) {
        val nodes = node.nodes
        var i = 0
        while (i < nodes.size - 1) {
            val it = processor.process(nodes[i], ctx, false)
            if (it == null)
                nodes.removeAt(i)
            else nodes[i++] = it
        }
        val it = processor.process(nodes.removeAt(nodes.lastIndex), ctx, valMode)
        if (it != null) {
            nodes += it
        }
    }
}


fun nodeProgn(info: INodeInfo, nodes: MutableList<Node>) =
    NodeNodesList(info.withType(NodeTypes.PROGN), nodes)

/**
 * Создаёт новую коллекцию контекстов,
 *
 * С текущим модулем.
 */
fun <T : IContextCollection<T>> T.with(ctx: Module) =
    this.with(MODULE, ctx)

/**
 * Создаёт новую коллекцию контекстов,
 *
 * С текущей платформой.
 */
fun <T : IContextCollection<T>> T.with(ctx: IPlatform) =
    this.with(PLATFORM, ctx)

/**
 * Наличие платформы в контексте.
 */
val IContextCollection<*>.isPlatform: Boolean
    get() = contexts.containsKey(PLATFORM)

/**
 * Текущий модуль.
 */
var IContextCollection<*>.module
    set(value) { contexts[MODULE] = value }
    get() = contexts[MODULE] as Module

/**
 * Платформа.
 */
var IContextCollection<*>.platform
    set(value) { this.contexts[PLATFORM] = value }
    get() = this.contexts[PLATFORM] as IPlatform

/**
 * Поставщик модулей.
 */
var IContextCollection<*>.mp
    set(value) { this.contexts[MODULE_PROVIDER] = value }
    get() = this.contexts[MODULE_PROVIDER] as ModulesProvider

/**
 * Поставщик типов.
 */
var IContextCollection<*>.tp
    set(value) { this.contexts[TYPES_PROVIDER] = value }
    get() = this.contexts[TYPES_PROVIDER] as TypesProvider