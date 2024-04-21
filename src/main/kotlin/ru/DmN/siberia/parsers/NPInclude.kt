package ru.DmN.siberia.parsers

import ru.DmN.pht.module.utils.Module
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.parser.ParserImpl
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parser.utils.file
import ru.DmN.siberia.processor.utils.module
import ru.DmN.siberia.utils.node.INodeInfo
import ru.DmN.siberia.utils.node.NodeTypes.PROGN
import java.io.File

object NPInclude : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node? {
        val module = ctx.module
        val file = parser.nextToken()!!.text!!
        if (file.endsWith('*')) {
            val nodes = ArrayList<Node>()
            val dir = file.substring(0, file.lastIndex - 1)
            File(module.name, dir).list()!!.forEach { it ->
                parse("$dir/$it", module, parser, ctx)?.let {
                    nodes += it
                }
            }
            return NodeNodesList(INodeInfo.of(PROGN, ctx, token), nodes)
        }
        return parse(file, module, parser, ctx)
    }

    private fun parse(file: String, module: Module, parser: Parser, ctx: ParsingContext): Node? =
        ParserImpl(String(module.getModuleFile(file).readBytes()), parser.mp).parseNode(ctx.subCtx().apply { this.file = "${module.name}/$file" })
}