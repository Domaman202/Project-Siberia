package ru.DmN.pht.module.utils

import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.Parser
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.utils.IPlatform

fun ModulesProvider.getOrLoadModule(name: String, platform: IPlatform): Module =
    this[name].let {
        if (it?.init != true)
            Parser(Module.getModuleFile(name), this).parseNode(ParsingContext.of(StdModule).apply { this.platform = platform })
        (it ?: this.getOrThrow(name))
    }