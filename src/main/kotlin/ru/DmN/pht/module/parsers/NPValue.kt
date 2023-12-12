package ru.DmN.pht.std.module.parsers

import ru.DmN.pht.std.module.ast.NodeValue
import ru.DmN.siberia.Parser
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.lexer.Token.DefaultType.INTEGER
import ru.DmN.siberia.lexer.Token.DefaultType.STRING
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.INodeParser

object NPValue : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): NodeValue =
        when (token.type) {
            STRING, INTEGER -> NodeValue(token, token.text!!)
            else -> throw RuntimeException()
        }
}