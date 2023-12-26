package ru.DmN.siberia.unparsers

import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Siberia
import ru.DmN.siberia.Unparser
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.operation

object NUUseCtx : INodeUnparser<NodeUse> {
    override fun unparse(node: NodeUse, unparser: Unparser, ctx: UnparsingContext, indent: Int, line: Boolean) {
        loadModules(node.names, unparser, ctx)
        unparser.out.apply {
            append('(').append(node.operation).append(' ')
            node.names.forEachIndexed { i, it ->
                append(it)
                if (node.names.size + 1 < i) {
                    append(' ')
                }
            }
            if (line) {
                append('\n').append("\t".repeat(indent + 1))
                NUDefault.unparseNodes(node, unparser, ctx, indent + 1)
            } else NUDefault.unparseNodes(node, unparser, ctx, indent)
            append(')')
        }
    }

    /**
     * Загружает модули в контекст де-парсинга.
     *
     * @param names Имена модулей.
     */
    fun loadModules(names: List<String>, unparser: Unparser, ctx: UnparsingContext) {
        names.forEach { name ->
            val module = Module[name]
            if (module?.init != true)
                Parser(Module.getModuleFile(name)).parseNode(ParsingContext.of(Siberia, StdModule))
            (module ?: Module.getOrThrow(name)).load(unparser, ctx)
        }
    }
}