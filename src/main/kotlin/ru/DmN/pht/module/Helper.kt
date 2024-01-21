package ru.DmN.pht.std.module

import ru.DmN.siberia.utils.Module
import ru.DmN.pht.std.module.parsers.NPArgument
import ru.DmN.pht.std.module.parsers.NPValue
import ru.DmN.pht.std.module.parsers.NPValueList

object Helper : Module("pht/module/helper") { // todo: unparsers
    init {
        add(Regex("author|class|deps|name|res|src|uses|version"), NPArgument)
        add(Regex("valn!"),  NPValueList)
        add(Regex("value!"), NPValue)
    }
}