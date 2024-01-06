package ru.DmN.siberia.processor.ctx

import ru.DmN.siberia.Siberia
import ru.DmN.siberia.ctx.IContextCollection
import ru.DmN.siberia.ctx.IContextKey
import ru.DmN.siberia.utils.*

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
    override val contexts: MutableMap<IContextKey, Any?> = HashMap()
) : IContextCollection<ProcessingContext> {
    /**
     * Создаёт под-контекст.
     */
    fun subCtx() =
        ProcessingContext(SubList(loadedModules), SubMap(contexts))


    override fun with(key: IContextKey, ctx: Any?): ProcessingContext =
        ProcessingContext(loadedModules, SubMap(contexts).apply { this[key] = ctx })

    companion object {
        /**
         * Создаёт базовый контекст.
         */
        fun base() =
            ProcessingContext(mutableListOf(Siberia))
    }
}