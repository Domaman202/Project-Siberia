package ru.DmN.pht.module.utils

import ru.DmN.siberia.parser.ParserImpl
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.utils.IPlatform

fun ModulesProvider.getOrLoadModule(name: String, platform: IPlatform): Module =
    this[name].let {
        if (it?.init != true)
            ParserImpl(Module.getModuleFile(name), this).parseNode(ParsingContext.module(platform, "$name/module.pht"))
        (it ?: this.getOrThrow(name))
    }