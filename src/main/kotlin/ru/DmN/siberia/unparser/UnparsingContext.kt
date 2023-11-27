package ru.DmN.siberia.unparser

import ru.DmN.siberia.Siberia
import ru.DmN.siberia.utils.Module

/**
 * Контекст де-парсинга.
 */
class UnparsingContext(
    /**
     * Загруженные модули.
     */
    val loadedModules: MutableList<Module> = ArrayList()
) {
    companion object {
        fun base(): UnparsingContext =
            UnparsingContext(mutableListOf(Siberia))
    }
}