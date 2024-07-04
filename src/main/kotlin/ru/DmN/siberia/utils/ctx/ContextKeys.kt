package ru.DmN.siberia.utils.ctx

enum class ContextKeys : IContextKey {
    /**
     * (Compiler)
     * Список собранных модулей.
     */
    COMPILED_MODULES,

    /**
     * (Compiler)
     * Режим раздельной сборки модулей.
     */
    SPLIT_MODULE_BUILD
}