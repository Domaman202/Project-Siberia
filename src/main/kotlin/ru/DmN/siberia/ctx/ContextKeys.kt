package ru.DmN.siberia.ctx

/**
 * Стандартные ключи контекстов.
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
     * Пул парсеров.
     */
    PARSERS_POOL,

    /**
     * Целевая (и/или текущая) версия java классов.
     *
     * [Java Class Version]
     */
    JCV,

    /**
     * Текущий файл.
     */
    FILE
}