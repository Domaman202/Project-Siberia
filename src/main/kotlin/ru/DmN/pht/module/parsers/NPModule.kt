package ru.DmN.pht.std.module.parsers

import ru.DmN.pht.module.node.NodeTypes.MODULE
import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.std.module.Helper
import ru.DmN.pht.std.module.ast.NodeArgument
import ru.DmN.pht.std.module.ast.NodeModule
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.NPProgn
import ru.DmN.siberia.parsers.NPUseCtx.loadModules
import ru.DmN.siberia.parsers.SimpleNP
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.utils.node.INodeInfo

object NPModule : SimpleNP(MODULE) {
    @Suppress("UNCHECKED_CAST")
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val context = ctx.subCtx()
        context.loadedModules.add(0, Helper)
        return NPProgn.parse(parser, context) { it ->
            NodeModule(INodeInfo.of(MODULE, ctx, token), it.associate { it as NodeArgument; Pair(it.name, it.value) }).apply {
                val name = data["name"] as String
                module = parser.mp.getOrPut(name) {
                    if (data["class"] == null)
                        Module(name)
                    else Class.forName(data["class"] as String).getField("INSTANCE").get(null) as Module
                }
                if (!module.init) {
                    (data["author"] as String?)?.let { module.author = it }
                    (data["deps"] as List<String>?)?.let {
                        module.deps += it
                        parser.mp.loadModules(it.toMutableList(), parser, ctx)
                    }
                    (data["res"] as List<String>?)?.let { module.resources += it }
                    (data["src"] as List<String>?)?.let { module.sources += it }
                    (data["uses"] as List<String>?)?.let { module.uses += it }
                    (data["version"] as String?)?.let { module.version = it }
                    module.init(ctx.platform, parser.mp)
                }
            }
        }
    }
}