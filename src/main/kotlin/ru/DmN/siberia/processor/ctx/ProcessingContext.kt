package ru.DmN.siberia.processor.ctx

import ru.DmN.pht.module.utils.Module
import ru.DmN.siberia.Siberia
import ru.DmN.siberia.utils.ctx.IContextCollection
import ru.DmN.siberia.utils.ctx.IContextKey
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