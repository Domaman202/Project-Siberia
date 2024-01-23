package ru.DmN.pht.module.utils

import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Unparser
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.processor.utils.Platforms
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.utils.Module

fun getOrLoadModule(name: String): Module =
    Module[name].let {
        if (it?.init != true)
            Parser(Module.getModuleFile(name)).parseNode(ParsingContext.of(StdModule).apply { platform = Platforms.UNIVERSAL })
        (it ?: Module.getOrThrow(name))
    }

fun loadModule(name: String, unparser: Unparser, ctx: UnparsingContext) {
    val module = Module[name]
    if (module?.init != true)
        Parser(Module.getModuleFile(name)).parseNode(ParsingContext.of(StdModule).apply { platform = Platforms.UNIVERSAL })
    (module ?: Module.getOrThrow(name)).load(unparser, ctx)
}