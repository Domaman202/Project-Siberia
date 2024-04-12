package ru.DmN.siberia.parser.ctx

import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.Siberia
import ru.DmN.siberia.parser.utils.file
import ru.DmN.siberia.parser.utils.parsersPool
import ru.DmN.siberia.utils.ctx.IContextCollection
import ru.DmN.siberia.utils.ctx.IContextKey
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.utils.*
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
         * Создаёт контекст для парсинга заголовка модуля (module.pht).
         *
         * @param platform Целевая платформа.
         */
        fun module(platform: IPlatform) =
            of(StdModule).apply { this.platform = platform }

        /**
         * Создаёт контекст для парсинга заголовка модуля (module.pht).
         *
         * @param platform Целевая платформа.
         * @param file Путь до module.pht.
         */
        fun module(platform: IPlatform, file: String) =
            of(StdModule).apply { this.platform = platform; this.file = file }

        /**
         * Создаёт базовый контекст с набором модулей.
         */
        fun of(vararg list: Module) =
            base().apply { loadedModules += list }
    }
}