package ru.DmN.siberia.parsers

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.lexer.Token.DefaultType.OPERATION
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.NPUseCtx.parse
import ru.DmN.siberia.utils.node.INodeInfo
import ru.DmN.siberia.utils.node.NodeTypes.LOAD_CTX

object NPLoadCtx : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val names = ArrayList<String>()
        var tk = parser.nextToken()!!
        while (tk.type == OPERATION) {
            names.add(tk.text!!)
            tk = parser.nextToken()!!
        }
        parser.pushToken(tk)
        return parser.mp.parse(names, parser, ctx) { p, c -> NPProgn.parse(p, c) { NodeUse(INodeInfo.of(LOAD_CTX, ctx, token), it, names) } }
    }
}