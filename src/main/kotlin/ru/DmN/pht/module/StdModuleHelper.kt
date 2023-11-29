package ru.DmN.pht.std.module

import ru.DmN.siberia.utils.Module
import ru.DmN.pht.std.module.parsers.NPArgument
import ru.DmN.pht.std.module.parsers.NPValue
import ru.DmN.pht.std.module.parsers.NPValueList

object StdModuleHelper : Module("pht/module/helper") {
    init {
        add("name|version|files|class|deps|uses|author".toRegex(), NPArgument)
        add("valn!",    NPValueList)
        add("value!",   NPValue)
    }
}