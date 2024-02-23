package ru.DmN.pht.module.utils

import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Unparser
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.processor.utils.Platforms
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.ModulesProvider

fun ModulesProvider.getOrLoadModule(name: String): Module =
    this[name].let {
        if (it?.init != true)
            Parser(Module.getModuleFile(name), this).parseNode(ParsingContext.of(StdModule).apply { platform = Platforms.UNIVERSAL })
        (it ?: this.getOrThrow(name))
    }

fun ModulesProvider.loadModule(name: String, unparser: Unparser, ctx: UnparsingContext) {
    val module = this[name]
    if (module?.init != true)
        Parser(Module.getModuleFile(name), this).parseNode(ParsingContext.of(StdModule).apply { platform = Platforms.UNIVERSAL })
    (module ?: this.getOrThrow(name)).load(unparser, ctx)
}