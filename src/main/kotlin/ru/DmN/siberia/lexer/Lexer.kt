package ru.DmN.siberia.lexer

/**
 * Абстракция Лексического анализатора.
 */
abstract class Lexer : Iterator<Token?> {
    /**
     * Проверка на наличие следующего токена.
     */
    abstract override fun hasNext(): Boolean

    /**
     * Выдаёт следующий токен если таковой есть, в противном случае null.
     */
    abstract override fun next(): Token?
}