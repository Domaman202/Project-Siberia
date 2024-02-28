package ru.DmN.siberia.processor.ctx

import ru.DmN.siberia.utils.ctx.IContextKey

/**
 * Стандартные ключи контекста для обработчика.
 */
enum class ContextKeys : IContextKey {
    /**
     * Текущий модуль.
     */
    MODULE,

    /**
     * Текущая целевая платформа.
     */
    PLATFORM,

    /**
     * Текущий поставщик модулей.
     */
    MODULE_PROVIDER,

    /**
     * Текущий поставщик типов.
     */
    TYPES_PROVIDER
}