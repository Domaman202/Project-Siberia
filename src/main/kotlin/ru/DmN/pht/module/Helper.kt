package ru.DmN.pht.std.module

import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.std.module.parsers.NPArgument
import ru.DmN.pht.std.module.parsers.NPValue
import ru.DmN.pht.std.module.parsers.NPValueList

object Helper : Module("pht/module/helper") { // todo: unparsers
    init {
        init = true
        add(Regex("author|class|deps|name|res|src|uses|version"), NPArgument)
        add(Regex("valn!"),  NPValueList)
        add(Regex("value!"), NPValue)
    }
}