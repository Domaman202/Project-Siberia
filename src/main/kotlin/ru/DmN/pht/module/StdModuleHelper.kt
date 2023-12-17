package ru.DmN.pht.std.module

import ru.DmN.siberia.utils.Module
import ru.DmN.pht.std.module.parsers.NPArgument
import ru.DmN.pht.std.module.parsers.NPValue
import ru.DmN.pht.std.module.parsers.NPValueList

object StdModuleHelper : Module("pht/module/helper") { // todo: unparsers
    init {
        add(Regex("name|version|files|class|deps|uses|author"), NPArgument)
        add(Regex("valn!"),  NPValueList)
        add(Regex("value!"), NPValue)
    }
}