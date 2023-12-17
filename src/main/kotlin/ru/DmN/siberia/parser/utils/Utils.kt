package ru.DmN.siberia.parser.utils

import ru.DmN.siberia.Parser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.lexer.Token.DefaultType.*
import ru.DmN.siberia.parser.ctx.ParsingContext
import java.util.*

/**
 * Стандартная функция парсинга.
 */
fun Parser.baseParseNode(ctx: ParsingContext): Node? {
    val startToken = nextToken() ?: return null
    return when (startToken.type) {
        OPEN_BRACKET -> pnb {
            val operationToken = nextToken()!!
            when (operationToken.type) {
                OPEN_BRACKET -> {
                    tokens.push(operationToken)
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
            tokens.push(startToken)
            null
        }
    }
}

fun Parser.parseProgn(ctx: ParsingContext, token: Token) =
    get(ctx, "progn")!!.parse(this, ctx, token)
fun Parser.parseValn(ctx: ParsingContext, token: Token) =
    get(ctx, "valn!")!!.parse(this, ctx, token)
fun Parser.parseMacro(ctx: ParsingContext, token: Token) =
    get(ctx, "macro")!!.parse(this, ctx, token)
fun Parser.parseMCall(ctx: ParsingContext, token: Token) =
    get(ctx, "mcall!")!!.parse(this, ctx, token)
fun Parser.parseValue(ctx: ParsingContext, token: Token) =
    get(ctx, "value!")!!.parse(this, ctx, token)!!

/**
 * Пул функций парсинга.
 *
 * При смене функции парсинга старая функция помещается суда (push),
 * После чего может быть возвращена обратно (pop).
 */
var ParsingContext.parsersPool
    set(value) { this.contexts["siberia/pp"] = value }
    get() = this.contexts["siberia/pp"] as Stack<Parser.(ctx: ParsingContext) -> Node?>

/**
 * Текущий файл.
 */
var ParsingContext.file
    set(value) { this.contexts["siberia/file"] = value }
    get() = this.contexts["siberia/file"] as String