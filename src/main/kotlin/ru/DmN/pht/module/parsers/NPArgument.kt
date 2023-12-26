package ru.DmN.pht.std.module.parsers

import ru.DmN.pht.module.node.NodeTypes
import ru.DmN.pht.std.module.ast.IValueNode
import ru.DmN.pht.std.module.ast.NodeArgument
import ru.DmN.siberia.Parser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.INodeParser

object NPArgument : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node =
        NodeArgument(INodeInfo.of(NodeTypes.VALUE, ctx, token), token.text!!, (parser.parseNode(ctx) as IValueNode).value)
}