package ru.DmN.siberia.utils

import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.utils.Module.Companion.toRegularExpr

fun Module.add(text: String, parser: INodeParser) =
    this.add(text.toRegularExpr(), parser)