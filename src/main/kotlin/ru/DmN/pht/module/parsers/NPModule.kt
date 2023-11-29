package ru.DmN.pht.std.module.parsers

import ru.DmN.siberia.Parser
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.parsers.NPDefault
import ru.DmN.siberia.parsers.SimpleNP
import ru.DmN.siberia.ups.NUPUseCtx
import ru.DmN.siberia.utils.Module
import ru.DmN.pht.std.module.StdModuleHelper
import ru.DmN.pht.std.module.ast.IValueNode
import ru.DmN.pht.std.module.ast.NodeModule

object NPModule : SimpleNP() {
    override fun parse(parser: Parser, ctx: ParsingContext, operationToken: Token): Node {
        val context = ctx.subCtx()
        context.loadedModules.add(0, StdModuleHelper)
        return NPDefault.parse(parser, context) { it ->
            NodeModule(operationToken, it.associate { Pair(it.token.text!!, (it as IValueNode).value) }).apply {
                val name = data["name"] as String
                module = Module.getOrPut(name) {
                    if (data["class"] == null)
                        Module(name)
                    else Class.forName(data["class"] as String).getField("INSTANCE").get(null) as Module
                }
                if (!module.init) {
                    module.init = true
                    (data["version"] as String?)?.let { module.version = it }
                    (data["deps"] as List<String>?)?.let {
                        module.deps += it
                        NUPUseCtx.loadModules(it, parser, ctx)
                    }
                    (data["uses"] as List<String>?)?.let { module.uses += it }
                    (data["files"] as List<String>?)?.let { module.files += it }
                    (data["author"] as String?)?.let { module.author = it }
                }
            }
        }
    }

}