package ru.DmN.pht.std.module

import ru.DmN.pht.std.module.parsers.NPModule
import ru.DmN.siberia.utils.Module

object StdModule : Module("pht/module") { // todo: unparsers
    init {
        init = true
    }

    override fun initParsers() {
        add(Regex("module"), NPModule)
    }
}