package ru.DmN.siberia.unparser

import ru.DmN.siberia.Siberia
import ru.DmN.siberia.utils.ctx.IContextCollection
import ru.DmN.siberia.utils.ctx.IContextKey
import ru.DmN.pht.module.utils.Module
import ru.DmN.siberia.utils.SubMap

/**
 * Контекст де-парсинга.
 */
class UnparsingContext(
    /**
     * Загруженные модули.
     */
    val loadedModules: MutableList<Module> = ArrayList(),
    /**
     * Контексты
     */
    override val contexts: MutableMap<IContextKey, Any?> = HashMap()
) : IContextCollection<UnparsingContext> {
    override fun with(key: IContextKey, ctx: Any?): UnparsingContext =
        UnparsingContext(loadedModules, SubMap(contexts).apply { this[key] = ctx })

    companion object {
        fun base(): UnparsingContext =
            UnparsingContext(mutableListOf(Siberia))
    }
}