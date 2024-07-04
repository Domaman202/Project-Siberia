package ru.DmN.pht.std.module.parsers

import ru.DmN.pht.module.node.NodeTypes.MODULE
import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.std.module.Helper
import ru.DmN.pht.std.module.ast.NodeArgument
import ru.DmN.pht.std.module.ast.NodeModule
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.NPProgn
import ru.DmN.siberia.parsers.NPUseCtx.loadModules
import ru.DmN.siberia.parsers.SimpleNP
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.utils.IPlatform
import ru.DmN.siberia.utils.node.INodeInfo

object NPModule : SimpleNP(MODULE) {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val context = ctx.subCtx()
        context.loadedModules.add(0, Helper)
        return NPProgn.parse(parser, context) { it ->
            NodeModule(INodeInfo.of(MODULE, ctx, token), it.associate { it as NodeArgument; Pair(it.name, it.value) }).apply {
                val name = data["name"] as String
                module = parser.mp.getOrPut(name) {
                    data.get<String?>("class").let {
                        it ?: return@let Module(name)
                        Class.forName(it).getField("INSTANCE").get(null) as Module
                    }
                }
                module.apply {
                    if (!init) {
                        data["author"]<String> { author = it }
                        data["deps"]<List<String>> {
                            deps += it
                            parser.mp.loadModules(it.toMutableList(), parser, ctx)
                        }
                        data["loads"]<List<String>> { loads += it }
                        data["platform"]<String> { platform = IPlatform[it] }
                        data["res"]<List<String>> { resources += it }
                        data["src"]<List<String>> { sources += it }
                        data["uses"]<List<String>> { uses += it }
                        data["version"]<String> { version = it }
                        init(ctx.platform, parser.mp)
                    }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private inline operator fun <T> Any?.invoke(block: (T) -> Unit) {
        this ?: return
        block(this as T)
    }

    @Suppress("UNCHECKED_CAST", "EXTENSION_SHADOWED_BY_MEMBER", "NOTHING_TO_INLINE")
    private inline fun <R> Map<String, Any?>.get(key: String): R =
        get(key) as R
}