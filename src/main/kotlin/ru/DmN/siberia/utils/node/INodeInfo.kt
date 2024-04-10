package ru.DmN.siberia.utils.node

import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parser.utils.file
import java.io.InputStream
import java.util.function.Function

/**
 * Информация о ноде
 */
interface INodeInfo {
    /**
     * Тип ноды.
     */
    val type: INodeType

    /**
     * Информация о токене.
     */
    val ti: ITokenInfo?

    /**
     * Возвращает информацию с изменённым типом.
     */
    fun withType(type: INodeType): INodeInfo

    /**
     * Выводит всю информацию с сообщением.
     * @param message Сообщение.
     * @param provider Поставщик файлов, укажите если хотите получить пометку в исходном коде.
     */
    fun print(message: String, provider: Function<String, InputStream>?): String =
        "$message\n${print(provider)}"

    /**
     * Выводит всю информацию.
     * @param provider Поставщик файлов, укажите если хотите получить пометку в исходном коде.
     */
    fun print(provider: Function<String, InputStream>?): String

    companion object {
        /**
         * Создаёт информацию о ноде, указывает имя файла и номер строки на котором получена нода.
         *
         * @param type Тип ноды.
         * @param ctx Контекст парсинга.
         * @param token Токен ноды.
         */
        fun of(type: INodeType, ctx: ParsingContext, token: Token) =
            token.text!!.length.let { l -> NodeInfoImpl(type, ctx.file?.let { f -> TokenInfoImpl(f, token.line, token.ptr - l, l) }) }

        /**
         * Создаёт информацию о ноде.
         *
         * @param type Тип ноды.
         */
        fun of(type: INodeType) =
            NodeInfoImpl(type, null)
    }
}