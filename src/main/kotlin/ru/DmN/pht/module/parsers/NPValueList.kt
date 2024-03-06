package ru.DmN.pht.std.module.parsers

import ru.DmN.pht.module.node.NodeTypes
import ru.DmN.pht.std.module.ast.IValueNode
import ru.DmN.pht.std.module.ast.NodeValueList
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.lexer.Token.DefaultType.*
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parser.utils.parseValue
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.utils.node.INodeInfo

object NPValueList : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node =
        parse(parser, ctx) { it -> NodeValueList(INodeInfo.of(NodeTypes.VALUE_LIST, ctx, token), it.map { (it as IValueNode).value }) }

    private fun parse(parser: Parser, ctx: ParsingContext, constructor: (it: MutableList<Node>) -> Node): Node {
        val nodes = ArrayList<Node>()
        var tk = parser.nextToken()
        while (tk != null && tk.type != CLOSE_CBRACKET) {
            nodes.add(
                if (tk.type == OPEN_BRACKET || tk.type == OPEN_CBRACKET) {
                    parser.pushToken(tk)
                    parser.parseNode(ctx)!!
                } else parser.parseValue(ctx, tk)
            )
            tk = parser.nextToken()
        }
        return constructor(nodes)
    }
}