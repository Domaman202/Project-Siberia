package ru.DmN.siberia.utils

import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.unparsers.INodeUnparser

fun Module.addb(name: String, up: INUPC<*, *, *>?) {
    adda(name, up as INUP<*, *>)
    add(name, up as INodeCompiler<*>)
}

fun Module.adda(name: String, up: INUP<*, *>?) =
    add(name, up, up, up)
fun Module.adda(name: String, up: INUP<*, *>?, parser: INodeParser) =
    add(name, parser, up, up)
fun Module.adda(name: String, up: INUP<*, *>?, unparser: INodeUnparser<*>) =
    add(name, up, unparser, up)
fun Module.adda(name: String, up: INUP<*, *>?, processor: INodeProcessor<*>) =
    add(name, up, up, processor)

fun Module.addb(name: Regex, up: INUPC<*, *, *>?) {
    add(name, up as INUP<*, *>)
    add(name, up as INodeCompiler<*>)
}

fun Module.adda(name: Regex, up: INUP<*, *>?) =
    add(name, up, up, up)
fun Module.adda(name: Regex, up: INUP<*, *>?, parser: INodeParser) =
    add(name, parser, up, up)
fun Module.adda(name: Regex, up: INUP<*, *>?, unparser: INodeUnparser<*>) =
    add(name, up, unparser, up)
fun Module.adda(name: Regex, up: INUP<*, *>?, processor: INodeProcessor<*>) =
    add(name, up, up, processor)