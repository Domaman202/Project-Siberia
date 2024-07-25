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
     * Определение макросов.
     */
    MACROS_DEFINE,

    /**
     * Импорт макросов.
     */
    MACROS_IMPORT,

    /**
     * Импорт типов.
     */
    TYPES_IMPORT,

    /**
     * Предопределение типов.
     */
    TYPES_PREDEFINE,

    /**
     * Определение типов.
     */
    TYPES_DEFINE,

    /**
     * Импорт расширений.
     */
    EXTENSIONS_IMPORT,

    /**
     * Обработка тел методов.
     */
    METHODS_BODY,

    /**
     * Окончание всей обработки.
     */
    FINALIZATION
}