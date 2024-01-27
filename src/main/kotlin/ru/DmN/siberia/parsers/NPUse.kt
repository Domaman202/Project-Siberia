package ru.DmN.siberia.parsers

import ru.DmN.siberia.Parser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.parser.ctx.ParsingContext

object NPUse : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val names = ArrayList<String>()
        var tk = parser.nextToken()!!
        while (tk.type == Token.DefaultType.OPERATION) {
            names.add(tk.text!!)
            tk = parser.nextToken()!!
        }
        parser.tokens.push(tk)
        return parse(names, token, parser, ctx)
    }

    fun parse(names: MutableList<String>, token: Token, parser: Parser, ctx: ParsingContext): NodeUse {
        NPUseCtx.loadModules(names, parser, ctx)
        return NodeUse(INodeInfo.of(NodeTypes.USE, ctx, token), ArrayList(), names)
    }
}