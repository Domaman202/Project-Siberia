package ru.DmN.siberia.parser.ctx

import ru.DmN.siberia.Siberia
import ru.DmN.siberia.parser.utils.parsersPool
import ru.DmN.siberia.ctx.IContextCollection
import ru.DmN.siberia.ctx.IContextKey
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.SubMap
import ru.DmN.siberia.utils.SubList
import java.util.*
import kotlin.collections.HashMap

/**
 * Контекст парсинга
 */
class ParsingContext (
    /**
     * Загруженные модули
     */
    val loadedModules: MutableList<Module> = ArrayList(),
    /**
     * Контексты
     */
    override val contexts: MutableMap<IContextKey, Any?> = HashMap()
) : IContextCollection<ParsingContext> {
    /**
     * Создаёт под-контекст
     */
    fun subCtx() =
        ParsingContext(SubList(loadedModules), SubMap(contexts))

    override fun with(key: IContextKey, ctx: Any?): ParsingContext =
        ParsingContext(SubList(loadedModules), SubMap(contexts).apply { this[key] = ctx })

    companion object {
        /**
         * Создаёт базовый контекст.
         */
        fun base() =
            ParsingContext(mutableListOf(Siberia)).apply { this.parsersPool = Stack() }

        /**
         * Создаёт базовый контекст с набором модулей.
         */
        fun of(vararg list: Module) =
            base().apply { loadedModules += list }
    }
}