package ru.DmN.siberia.compiler.utils

import ru.DmN.siberia.utils.stage.IStage

/**
 * Стадии компиляции.
 */
enum class CompilingStage : IStage {
    /**
     * Неизвестная.
     */
    UNKNOWN,

    /**
     * Предопределение типов.
     */
    TYPES_PREDEFINE,

    /**
     * Определение типов.
     */
    TYPES_DEFINE,

    /**
     * Тела методов.
     */
    METHODS_BODY
}