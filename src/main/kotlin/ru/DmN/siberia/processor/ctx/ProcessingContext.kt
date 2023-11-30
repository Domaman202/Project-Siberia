package ru.DmN.siberia.processor.ctx

import ru.DmN.siberia.Siberia
import ru.DmN.siberia.utils.IContextCollection
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.SubList
import ru.DmN.siberia.utils.SubMap

/**
 * Контекст обработки.
 */
class ProcessingContext(
    /**
     * Загруженные модули.
     */
    val loadedModules: MutableList<Module> = ArrayList(),
    /**
     * Контексты
     */
    override val contexts: MutableMap<String, Any?> = HashMap()
) : IContextCollection<ProcessingContext> {
    /**
     * Создаёт под-контекст.
     */
    fun subCtx() =
        ProcessingContext(SubList(loadedModules), SubMap(contexts))

    /**
     * Создаёт под-контекст с общими модульными зависимостями.
     * Добавляет новый элемент контекста.
     *
     * @param name Имя нового элемента контекста.
     * @param ctx Новый элемент контекста.
     */
    override fun with(name: String, ctx: Any?): ProcessingContext =
        ProcessingContext(loadedModules, contexts.toMutableMap().apply { this[name] = ctx })
    companion object {
        /**
         * Создаёт базовый контекст.
         */
        fun base() =
            ProcessingContext(mutableListOf(Siberia))
    }
}