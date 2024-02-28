package ru.DmN.siberia.compiler.ctx

import ru.DmN.siberia.utils.ctx.IContextKey

/**
 * Стандартные ключи контекстов.
 */
enum class ContextKeys : IContextKey {
    /**
     * Целевая (и/или текущая) версия java классов.
     *
     * [Java Class Version]
     */
    JCV
}