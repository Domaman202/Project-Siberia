package ru.DmN.siberia.parsers

import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Siberia
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.utils.Module

object NPUseCtx : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val names = ArrayList<String>()
        var tk = parser.nextToken()!!
        while (tk.type == Token.DefaultType.OPERATION) {
            names.add(tk.text!!)
            tk = parser.nextToken()!!
        }
        parser.tokens.push(tk)
        return parse(names, parser, ctx) { context ->
            NPProgn.parse(parser, context) { NodeUse(INodeInfo.of(NodeTypes.USE_CTX, ctx, token), names, it) }
        }
    }

    fun parse(names: List<String>, parser: Parser, ctx: ParsingContext, parse: (context: ParsingContext) -> Node): Node {
        val context = ctx.subCtx()
        loadModules(names, parser, context)
        val node = parse(context)
        context.loadedModules.filter { names.contains(it.name) }.forEach { it.clear(parser, context) } // todo: clear?
        return node
    }

    /**
     * Загружает модули в контекст парсинга.
     *
     * @param names Имена модулей.
     */
    fun loadModules(names: List<String>, parser: Parser, ctx: ParsingContext) {
        names.forEach {
            val module = Module[it]
            if (module?.init != true)
                Parser(Module.getModuleFile(it)).parseNode(ParsingContext.of(Siberia, StdModule))
            (module ?: Module.getOrThrow(it)).load(parser, ctx)
        }
    }
}
