package ru.DmN.siberia.utils.node

import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parser.utils.file

/**
 * Информация о ноде
 */
interface INodeInfo {
    /**
     * Тип ноды.
     */
    val type: INodeType

    /**
     * Файл из которого получена нода.
     */
    val file: String?

    /**
     * Строка на которой была получена нода.
     */
    val line: Int?

    /**
     * Возвращает информацию с изменённым типом.
     */
    fun withType(type: INodeType): INodeInfo

    /**
     * Выводит всю информацию с сообщением.
     * @param message Сообщение
     */
    fun print(message: String): String =
        "$message\n${print()}"

    /**
     * Выводит всю информацию.
     */
    fun print(): String

    companion object {
        /**
         * Создаёт информацию о ноде, указывает имя файла и номер строки на котором получена нода.
         *
         * @param type Тип ноды.
         * @param ctx Контекст парсинга.
         * @param token Токен ноды.
         */
        fun of(type: INodeType, ctx: ParsingContext, token: Token) =
            NodeInfoImpl(type, ctx.file, token.line)

        /**
         * Создаёт информацию о ноде.
         *
         * @param type Тип ноды.
         */
        fun of(type: INodeType) =
            NodeInfoImpl(type, null, null)
    }
}