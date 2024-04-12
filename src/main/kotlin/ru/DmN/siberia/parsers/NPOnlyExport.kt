package ru.DmN.siberia.parsers

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.processor.utils.module
import ru.DmN.siberia.utils.node.NodeTypes.ONLY_EXPORT

object NPOnlyExport : SimpleNP(ONLY_EXPORT) {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node =
        super.parse(parser, ctx, token)!!.apply { ctx.module.exports += this as NodeNodesList }
}