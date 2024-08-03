package ru.DmN.siberia.processor.utils

import ru.DmN.siberia.utils.stage.IStage

/**
 * Стадии обработки.
 */
enum class ProcessingStage : IStage {
    /**
     * Неизвестная.
     */
    UNKNOWN,

    /**
     * После инициализации модуля. (Я если честно не помню зачем оно надо, но видимо надо)
     */
    MODULE_POST_INIT,

    /**
     * Окончание всей обработки.
     */
    FINALIZATION
}