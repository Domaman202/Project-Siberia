package ru.DmN.siberia.processor

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ProcessingStage
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.utils.ctx.IContextKey
import ru.DmN.siberia.utils.stage.StageManager
import ru.DmN.siberia.utils.vtype.TypesProvider
import ru.DmN.siberia.utils.vtype.VirtualType

/**
 * Абстракция обработчика.
 */
abstract class Processor {
    /**
     * Поставщик модулей.
     */
    abstract val mp: ModulesProvider
    /**
     * Поставщик типов.
     */
    abstract val tp: TypesProvider

    /**
     * Менеджер стадий обработки.
     */
    abstract val stageManager: StageManager<ProcessingStage>

    /**
     * Глобальные контексты.
     */
    abstract val contexts: MutableMap<IContextKey, Any?>

    /**
     * Вычисляет тип, который возвращает нода, в случае наличия такового, в противном случае возвращает null.
     */
    abstract fun calc(node: Node, ctx: ProcessingContext): VirtualType?

    /**
     * Обрабатывает ноду.
     */
    abstract fun process(node: Node, ctx: ProcessingContext, valMode: Boolean): Node?

    /**
     * Возвращает обработчик нод.
     */
    abstract fun get(node: Node, ctx: ProcessingContext): INodeProcessor<Node>
}