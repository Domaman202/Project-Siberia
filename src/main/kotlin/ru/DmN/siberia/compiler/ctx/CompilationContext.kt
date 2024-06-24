package ru.DmN.siberia.compiler.ctx

import ru.DmN.pht.module.utils.Module
import ru.DmN.siberia.Siberia
import ru.DmN.siberia.utils.SubList
import ru.DmN.siberia.utils.SubMap
import ru.DmN.siberia.utils.ctx.IContextCollection
import ru.DmN.siberia.utils.ctx.IContextKey

/**
 * Контекст компиляции.
 */
class CompilationContext(
    /**
     * Загруженные модули.
     */
    val loadedModules: MutableList<Module> = ArrayList(),
    /**
     * Контексты.
     */
    override val contexts: MutableMap<IContextKey, Any?> = HashMap()
) : IContextCollection<CompilationContext> {
    /**
     * Создаёт под-контекст.
     */
    fun subCtx() =
        CompilationContext(SubList(loadedModules), SubMap(contexts))

    override fun with(key: IContextKey, ctx: Any?): CompilationContext =
        CompilationContext(loadedModules, SubMap(contexts).apply { this[key] = ctx })

    companion object {
        /**
         * Создаёт базовый контекст.
         */
        fun base(): CompilationContext =
            CompilationContext(mutableListOf(Siberia))

        /**
         * Создаёт базовый контекст с набором модулей.
         */
        fun of(vararg list: Module) =
            base().apply { loadedModules += list }
    }
}