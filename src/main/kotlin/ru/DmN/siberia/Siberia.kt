package ru.DmN.siberia

import ru.DmN.siberia.compilers.NCDefault
import ru.DmN.siberia.compilers.NCUse
import ru.DmN.siberia.compilers.NCUseCtx
import ru.DmN.siberia.node.NodeTypes.*
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parser.utils.baseParseNode
import ru.DmN.siberia.parsers.NPExport
import ru.DmN.siberia.parsers.NPProgn
import ru.DmN.siberia.parsers.NPUse
import ru.DmN.siberia.parsers.NPUseCtx
import ru.DmN.siberia.processors.NRExport
import ru.DmN.siberia.processors.NRProgn
import ru.DmN.siberia.processors.NRUse
import ru.DmN.siberia.processors.NRUseCtx
import ru.DmN.siberia.unparsers.NUDefault
import ru.DmN.siberia.unparsers.NUUse
import ru.DmN.siberia.unparsers.NUUseCtx
import ru.DmN.siberia.utils.IPlatform.UNIVERSAL
import ru.DmN.siberia.utils.Module

object Siberia : Module("siberia") {
    private fun initParsers() {
        // e
        add(Regex("export"),  NPExport)
        // p
        add(Regex("progn"),   NPProgn)
        // u
        add(Regex("use"),     NPUse)
        add(Regex("use-ctx"), NPUseCtx)
    }

    private fun initUnparsers() {
        // e
        add(EXPORT,  NUDefault)
        // p
        add(PROGN,   NUDefault)
        add(PROGN_,  NUDefault)
        // u
        add(USE,     NUUse)
        add(USE_,    NUUse)
        add(USE_CTX, NUUseCtx)
        add(USE_CTX_,NUUseCtx)
    }

    private fun initProcessors() {
        // e
        add(EXPORT,  NRExport)
        // p
        add(PROGN,   NRProgn)
        add(PROGN_,  NRProgn)
        // u
        add(USE,     NRUse)
        add(USE_CTX, NRUseCtx)
    }

    private fun initCompilers() {
        // p
        add(UNIVERSAL, PROGN_,  NCDefault)
        // u
        add(UNIVERSAL, USE_,    NCUse)
        add(UNIVERSAL, USE_CTX_,NCUseCtx)
    }

    override fun load(parser: Parser, ctx: ParsingContext, uses: MutableList<String>) {
        parser.parseNode = { baseParseNode(it) }
    }

    init {
        initParsers()
        initUnparsers()
        initProcessors()
        initCompilers()
    }
}