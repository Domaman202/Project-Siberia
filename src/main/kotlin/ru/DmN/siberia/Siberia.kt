package ru.DmN.siberia

import ru.DmN.pht.module.utils.Module
import ru.DmN.siberia.compilers.NCDefault
import ru.DmN.siberia.compilers.NCUse
import ru.DmN.siberia.compilers.NCUseCtx
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.parser.ParserImpl
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.*
import ru.DmN.siberia.processors.NRExport
import ru.DmN.siberia.processors.NRProgn
import ru.DmN.siberia.processors.NRUse
import ru.DmN.siberia.processors.NRUseCtx
import ru.DmN.siberia.unparsers.NUDefault
import ru.DmN.siberia.unparsers.NUUse
import ru.DmN.siberia.unparsers.NUUseCtx
import ru.DmN.siberia.utils.IPlatform.UNIVERSAL
import ru.DmN.siberia.utils.node.NodeTypes.*

object Siberia : Module("siberia") {
    private fun initParsers() {
        // e
        add(Regex("export"),      NPExport)
        // i
        add(Regex("include"),     NPInclude)
        // o
        add(Regex("export-only"), NPExportOnly)
        // p
        add(Regex("progn"),       NPProgn)
        // u
        add(Regex("use"),         NPUse)
        add(Regex("use-ctx"),     NPUseCtx)
    }

    private fun initUnparsers() {
        // e
        add(EXPORT,      NUDefault)
        add(EXPORT_,     NUDefault)
        // o
        add(EXPORT_ONLY, NUDefault)
        // p
        add(PROGN,       NUDefault)
        add(PROGN_,      NUDefault)
        // u
        add(USE,         NUUse)
        add(USE_,        NUUse)
        add(USE_CTX,     NUUseCtx)
        add(USE_CTX_,    NUUseCtx)
    }

    private fun initProcessors() {
        // e
        add(EXPORT,  NRExport)
        add(EXPORT_, NRExport)
        // p
        add(PROGN,   NRProgn)
        add(PROGN_,  NRProgn)
        // u
        add(USE,     NRUse)
        add(USE_CTX, NRUseCtx)
    }

    private fun initCompilers() {
        // e
        add(UNIVERSAL, EXPORT_, NCDefault)
        // p
        add(UNIVERSAL, PROGN_,  NCDefault)
        // u
        add(UNIVERSAL, USE_,    NCUse)
        add(UNIVERSAL, USE_CTX_,NCUseCtx)
    }

    override fun load(parser: Parser, ctx: ParsingContext, uses: MutableList<String>): Boolean {
        if (!ctx.loadedModules.contains(this)) {
            super.load(parser, ctx, uses)
            return true
        }
        return false
    }

    override fun changeParser(parser: Parser, ctx: ParsingContext): Parser =
        ParserImpl(parser)

    init {
        initParsers()
        initUnparsers()
        initProcessors()
        initCompilers()
    }
}