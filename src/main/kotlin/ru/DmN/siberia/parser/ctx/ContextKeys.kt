package ru.DmN.siberia.parser.ctx

import ru.DmN.siberia.utils.ctx.IContextKey

/**
 * Стандартные ключи контекста для парсера.
 */
enum class ContextKeys : IContextKey {
    /**
     * Пул парсеров.
     */
    PARSERS_POOL,

    /**
     * Текущий файл.
     */
    FILE
}