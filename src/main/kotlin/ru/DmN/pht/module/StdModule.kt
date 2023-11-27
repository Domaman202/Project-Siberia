package ru.DmN.pht.std.module

import ru.DmN.pht.std.module.parsers.NPModule
import ru.DmN.siberia.ups.NUPDefault
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.adda

object StdModule : Module("pht/module", true) {
    init {
        adda("module", NUPDefault, NPModule)
    }
}