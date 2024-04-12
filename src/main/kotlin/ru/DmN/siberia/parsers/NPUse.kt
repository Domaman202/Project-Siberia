package ru.DmN.siberia.parsers

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.NPUseCtx.loadModules
import ru.DmN.siberia.utils.node.INodeInfo
import ru.DmN.siberia.utils.node.NodeTypes
import ru.DmN.siberia.utils.node.NodeTypes.USE

object NPUse : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val names = ArrayList<String>()
        var tk = parser.nextToken()!!
        while (tk.type == Token.DefaultType.OPERATION) {
            names.add(tk.text!!)
            tk = parser.nextToken()!!
        }
        parser.pushToken(tk)
        return parse(names, token, parser, ctx)
    }

    fun parse(names: MutableList<String>, token: Token, parser: Parser, ctx: ParsingContext): NodeUse {
        parser.mp.loadModules(names, parser, ctx)
        return NodeUse(INodeInfo.of(USE, ctx, token), ArrayList(), names)
    }
}