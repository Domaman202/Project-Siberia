package ru.DmN.siberia.parser.utils

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.parser.ctx.ContextKeys
import ru.DmN.siberia.parser.ctx.ParsingContext
import java.util.*

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
    set(value) { this.contexts[ContextKeys.PARSERS_POOL] = value }
    get() = this.contexts[ContextKeys.PARSERS_POOL] as Stack<Parser.(ctx: ParsingContext) -> Node?>

/**
 * Текущий файл.
 */
var ParsingContext.file
    set(value) { this.contexts[ContextKeys.FILE] = value }
    get() = this.contexts[ContextKeys.FILE] as String?