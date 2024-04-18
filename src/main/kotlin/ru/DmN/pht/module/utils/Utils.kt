package ru.DmN.pht.module.utils

import ru.DmN.siberia.parser.ParserImpl
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.utils.IPlatform
import java.io.File

fun List<String>.asFilesSequence(dir: String): Sequence<String> = object : Sequence<String> {
    override fun iterator(): Iterator<String> = object : Iterator<String> {
        private var i = 0
        private var j = -1
        private var sub: List<String>? = null

        override fun hasNext(): Boolean =
            sub?.let { j < it.size } ?: (i < size)

        override fun next(): String =
            sub?.let {
                if (j < it.size)
                    it[j++]
                else {
                    j = -1
                    sub = null
                    next()
                }
            } ?: get(i++).let { it ->
                if (it[it.lastIndex] == '*') {
                    j = 0
                    sub = it.substring(0, it.length - 2).let { File(dir, it).list()!!.map { file -> "$it/$file" } }
                    next()
                } else it
            }
    }
}

fun ModulesProvider.getOrLoadModule(name: String, platform: IPlatform): Module =
    this[name].let {
        if (it?.init != true)
            ParserImpl(Module.getModuleFile(name), this).parseNode(ParsingContext.module(platform, "$name/module.pht"))
        (it ?: this.getOrThrow(name))
    }