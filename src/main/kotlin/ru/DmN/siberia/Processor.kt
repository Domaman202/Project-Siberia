package ru.DmN.siberia

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.utils.ctx.IContextKey
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ProcessingStage
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.utils.stage.StupidStageManager
import ru.DmN.siberia.utils.vtype.TypesProvider
import ru.DmN.siberia.utils.vtype.VirtualType

/**
 * Обработчик.
 */
class Processor(
    /**
     * Поставщик модулей.
     */
    val mp: ModulesProvider,
    /**
     * Поставщик типов.
     */
    val tp: TypesProvider
) {
    /**
     * Менеджер стадий обработки.
     */
    val stageManager = StupidStageManager.of(ProcessingStage.UNKNOWN)

    /**
     * Глобальные контексты.
     */
    val contexts: MutableMap<IContextKey, Any?> = HashMap()

    /**
     * Вычисляет тип, который возвращает нода, в случае наличия такового, в противном случае возвращает null.
     */
    fun calc(node: Node, ctx: ProcessingContext): VirtualType? =
        get(node, ctx).calc(node, this, ctx)

    /**
     * Обрабатывает ноду.
     */
    fun process(node: Node, ctx: ProcessingContext, valMode: Boolean): Node? =
        if (node.info.type.processable)
            get(node, ctx).process(node, this, ctx, valMode)
        else node

    /**
     * Возвращает обработчик нод.
     */
    fun get(node: Node, ctx: ProcessingContext): INodeProcessor<Node> {
        val type = node.info.type
        ctx.loadedModules.forEach { it -> it.processors[type]?.let { return it as INodeProcessor<Node> } }
        throw RuntimeException("Processor for \"$type\" not founded!")
    }
}