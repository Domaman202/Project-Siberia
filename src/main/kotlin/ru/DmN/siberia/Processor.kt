package ru.DmN.siberia

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.processor.utils.ProcessingContext
import ru.DmN.siberia.processor.utils.ProcessingStage
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.utils.*

/**
 * Обработчик.
 */
class Processor(
    /**
     * Провайдер типов.
     */
    val tp: TypesProvider
) {
    /**
     * Список задач.
     */
    val tasks: DefaultEnumMap<ProcessingStage, MutableList<() -> Unit>> = DefaultEnumMap(ProcessingStage::class.java) { java.util.ArrayList() }

    /**
     * Глобальные контексты.
     */
    val contexts: MutableMap<String, Any?> = HashMap()

    /**
     * Вычисляет тип, который возвращает нода, в случае наличия такового, в противном случае возвращает null.
     */
    fun calc(node: Node, ctx: ProcessingContext): VirtualType? =
        get(node, ctx).calc(node, this, ctx)

    /**
     * Обрабатывает ноду.
     */
    fun process(node: Node, ctx: ProcessingContext, mode: ValType): Node? =
        get(node, ctx).process(node, this, ctx, mode)

    /**
     * Возвращает обработчик нод.
     */
    fun get(node: Node, ctx: ProcessingContext): INodeProcessor<Node> {
        val name = node.text
        val i = name.lastIndexOf('/')
        if (i < 1) {
            ctx.loadedModules.forEach { it -> it.processors.getRegex(name)?.let { return it as INodeProcessor<Node> } }
            throw RuntimeException("Processor for \"$name\" not founded!")
        } else {
            val module = name.substring(0, i)
            return ctx.loadedModules.find { it.name == module }!!.processors.getRegex(name.substring(i + 1)) as INodeProcessor<Node>
        }
    }

    /**
     * Помещает новую задачу в список.
     *
     * @param stage Стадия обработки.
     */
    fun pushTask(ctx: ProcessingContext, stage: ProcessingStage, task: () -> Unit) {
        if (stage.ordinal <= ctx.stage.get().ordinal)
            task()
        else tasks[stage] += task
    }
}