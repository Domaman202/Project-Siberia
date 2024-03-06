package ru.DmN.siberia.parser

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Lexer
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.INodeParser
import java.util.*

/**
 * Абстракция парсера.
 */
abstract class Parser {
    /**
     * Лексический анализатор.
     */
    abstract val lexer: Lexer

    /**
     * Поставщик модулей.
     */
    abstract val mp: ModulesProvider

    /**
     * Буфер токенов.
     */
    abstract val tokens: Stack<Token?>

    /**
     * Парсит ноду.
     */
    abstract fun parseNode(ctx: ParsingContext): Node?


    /**
     * Возвращает парсер нод.
     */
    abstract fun get(ctx: ParsingContext, name: String): INodeParser?

    /**
     * Возвращает следующий токен.
     *
     * @return токен, null - если токен отсутствует.
     */
    abstract fun nextToken(): Token?

    /**
     * Забирает токен назад.
     */
    abstract fun pushToken(token: Token?)
}