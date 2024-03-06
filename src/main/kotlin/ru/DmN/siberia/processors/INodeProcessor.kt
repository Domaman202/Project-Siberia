package ru.DmN.siberia.processors

import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.utils.vtype.VirtualType

/**
 * Обработчик нод.
 */
interface INodeProcessor<T : Node> {
    /**
     * Вычисляет тип, который возвращает нода, в случае наличия такового, в противном случае возвращает null.
     */
    fun calc(node: T, processor: Processor, ctx: ProcessingContext): VirtualType? =
        null

    /**
     * Обрабатывает ноду.
     */
    fun process(node: T, processor: Processor, ctx: ProcessingContext, valMode: Boolean): Node? =
        node
}