package ru.DmN.siberia.lexer

import ru.DmN.siberia.lexer.Token.DefaultType.OPERATION

/**
 * Токен
 */
data class Token(
    /**
     * Строка токена
     */
    val line: Int,
    /**
     * Тип токена
     */
    val type: IType,
    /**
     * Текст токена
     */
    val text: String? = null
) {
    companion object {
        /**
         * Создаёт токен операции
         */
        fun operation(line: Int, text: String) =
            Token(line, OPERATION, text)
    }

    /**
     * Тип токена
     */
    interface IType

    /**
     * Стандартные типы токена
     */
    enum class DefaultType : IType {
        OPEN_BRACKET,
        CLOSE_BRACKET,
        OPEN_CBRACKET,
        CLOSE_CBRACKET,

        OPERATION,

        PRIMITIVE,
        CLASS,
        CLASS_WITH_GEN,
        NAME_WITH_GEN,
        NAMING,

        NIL,
        BOOLEAN,
        CHAR,
        INTEGER,
        LONG,
        FLOAT,
        DOUBLE,
        STRING,
    }
}
