package ru.DmN.siberia.processor.utils

import ru.DmN.siberia.Processor
import ru.DmN.siberia.ast.INodesList
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ctx.ContextKeys
import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.ctx.IContextCollection
import ru.DmN.siberia.utils.Module

fun processNodesList(node: INodesList, processor: Processor, ctx: ProcessingContext, mode: ValType) {
    if (node.nodes.isNotEmpty()) {
        val nodes = node.nodes
        var i = 0
        while (i < nodes.size - 1) {
            val it = processor.process(nodes[i], ctx, ValType.NO_VALUE)
            if (it == null)
                nodes.removeAt(i)
            else nodes[i++] = it
        }
        val it = processor.process(nodes.removeAt(nodes.lastIndex), ctx, mode)
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
    this.with(ContextKeys.MODULE, ctx)

/**
 * Создаёт новую коллекцию контекстов,
 *
 * С текущей платформой.
 */
fun <T : IContextCollection<T>> T.with(ctx: Platforms) =
    this.with(ContextKeys.PLATFORM, ctx)

/**
 * Текущий модуль.
 */
var IContextCollection<*>.module
    set(value) { contexts[ContextKeys.MODULE] = value }
    get() = contexts[ContextKeys.MODULE] as Module

/**
 * Текущий модуль (если существует).
 */
var IContextCollection<*>.moduleOrNull
    set(value) { contexts[ContextKeys.MODULE] = value }
    get() = contexts[ContextKeys.MODULE] as Module?

/**
 * Платформа.
 */
var IContextCollection<*>.platform
    set(value) { this.contexts[ContextKeys.PLATFORM] = value }
    get() = this.contexts[ContextKeys.PLATFORM] as Platforms