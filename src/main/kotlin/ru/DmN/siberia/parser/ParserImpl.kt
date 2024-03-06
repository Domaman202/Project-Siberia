package ru.DmN.siberia.parser

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Lexer
import ru.DmN.siberia.lexer.LexerImpl
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.lexer.Token.DefaultType.*
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parser.utils.parseProgn
import ru.DmN.siberia.parser.utils.parseValn
import ru.DmN.siberia.parser.utils.parseValue
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.utils.getRegex
import java.util.*

/**
 * Стандартная реализация парсера.
 */
open class ParserImpl(override val lexer: Lexer, override val mp: ModulesProvider, override val tokens: Stack<Token?>) : Parser() {
    constructor(code: String, mp: ModulesProvider) : this(LexerImpl(code), mp, Stack())
    constructor(parser: Parser) : this(parser.lexer, parser.mp, parser.tokens)

    override fun parseNode(ctx: ParsingContext): Node? {
        val startToken = nextToken() ?: return null
        return when (startToken.type) {
            OPEN_BRACKET -> pnb {
                val operationToken = nextToken()!!
                when (operationToken.type) {
                    OPEN_BRACKET -> {
                        pushToken(operationToken)
                        parseProgn(ctx, operationToken)
                    }
                    OPERATION -> get(ctx, operationToken.text!!)!!.parse(this, ctx, operationToken)
                    else -> throw RuntimeException()
                }
            }
            OPEN_CBRACKET -> parseValn(ctx, startToken)
            OPERATION,
            PRIMITIVE,
            CLASS,
            NAMING,
            NIL,
            STRING,
            INTEGER,
            FLOAT,
            DOUBLE,
            BOOLEAN -> parseValue(ctx, startToken)
            else -> {
                pushToken(startToken)
                null
            }
        }
    }


    /**
     * Получает парсер нод.
     */
    override fun get(ctx: ParsingContext, name: String): INodeParser? {
        val i = name.lastIndexOf('/')
        return if (i < 1) {
            ctx.loadedModules.forEach { it -> it.parsers.getRegex(name)?.let { return it } }
            null
        } else {
            val module = name.substring(0, i)
            ctx.loadedModules.find { it.name == module }?.parsers?.getRegex(name.substring(i + 1))
        }
    }

    /**
     * Выполняет код в теле, после чего закрывает скобку.
     */
    protected inline fun <T> pnb(body: () -> T): T = body.invoke().apply { tryClose() }

    /**
     * Закрытие скобки
     */
    protected fun tryClose() {
        val token = nextToken()
        if (token?.type != CLOSE_BRACKET) {
            pushToken(token)
        }
    }

    override fun nextToken(): Token? {
        return if (tokens.empty())
            lexer.next()
        else tokens.pop()
    }

    override fun pushToken(token: Token?) {
        tokens.push(token)
    }
}