package ru.DmN.siberia.parser

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Lexer
import ru.DmN.siberia.lexer.LexerImpl
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.lexer.Token.DefaultType.*
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parser.utils.ParsingException
import ru.DmN.siberia.parser.utils.parseProgn
import ru.DmN.siberia.parser.utils.parseValn
import ru.DmN.siberia.parser.utils.parseValue
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.utils.exception.MessageException
import ru.DmN.siberia.utils.getRegex
import ru.DmN.siberia.utils.node.ErrorNodeType
import ru.DmN.siberia.utils.node.INodeInfo
import java.util.*

/**
 * Стандартная реализация парсера.
 */
open class ParserImpl(override val lexer: Lexer, override val mp: ModulesProvider, override val tokens: Stack<Token?>) : Parser() {
    constructor(code: String, mp: ModulesProvider) : this(LexerImpl(code), mp, Stack())
    constructor(parser: Parser) : this(parser.lexer, parser.mp, parser.tokens)

    override fun parseNode(ctx: ParsingContext): Node? {
        val stk = nextToken() ?: return null
        return when (stk.type) {
            OPEN_BRACKET -> pnb {
                val tk = nextToken()!!
                when (tk.type) {
                    OPEN_BRACKET -> {
                        pushToken(tk)
                        parseProgn(ctx, tk)
                    }
                    OPERATION -> get(ctx, tk.text!!)!!.parse(this, ctx, tk)
                    else -> throw ParsingException(MessageException(null, "Инструкция '${tk.text}' не найдена!"), INodeInfo.of(ErrorNodeType, ctx, tk))
                }
            }
            OPEN_CBRACKET -> parseValn(ctx, stk)
            OPERATION,
            PRIMITIVE,
            CLASS,
            NAMING,
            NIL,
            STRING,
            INTEGER,
            FLOAT,
            DOUBLE,
            BOOLEAN -> parseValue(ctx, stk)
            else -> {
                pushToken(stk)
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