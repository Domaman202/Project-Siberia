package ru.DmN.siberia.ups

import ru.DmN.siberia.Compiler
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Processor
import ru.DmN.siberia.Unparser
import ru.DmN.siberia.ast.*
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compilers.NCDefault
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.exports
import ru.DmN.siberia.processor.utils.isExports
import ru.DmN.siberia.processors.NRDefault
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.utils.INUPC
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.Variable
import ru.DmN.siberia.utils.text
import java.util.*

/**
 * Универсальный обработчик инструкции использования.
 */
object NUPUse : INUPC<NodeUse, NodeParsedUse, NodeProcessedUse> {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val names = ArrayList<String>()
        var tk = parser.nextToken()!!
        while (tk.type == Token.Type.OPERATION) {
            names.add(tk.text!!)
            tk = parser.nextToken()!!
        }
        parser.tokens.push(tk)
        return parse(names, token, parser, ctx)
    }

    fun parse(names: List<String>, token: Token, parser: Parser, ctx: ParsingContext): NodeParsedUse {
        NUPUseCtx.loadModules(names, parser, ctx)
        return NodeParsedUse(token, names, ArrayList())
    }

    override fun unparse(node: NodeUse, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        NUPUseCtx.loadModules(node.names, unparser, ctx)
        unparser.out.apply {
            append('(').append(node.text).append(' ')
            node.names.forEach(this::append)
            append(')')
        }
    }

    override fun process(node: NodeParsedUse, processor: Processor, ctx: ProcessingContext, mode: ValType): Node {
        val exports = ArrayList<NodeNodesList>()
        val processed = ArrayList<Node>()
        NUPUseCtx.injectModules(node, processor, ctx, ValType.NO_VALUE, processed).forEach {
            it.exports.forEach { exports += NRDefault.process(it, processor, ctx, ValType.NO_VALUE) }
        }
        return NodeProcessedUse(node.token, node.names, ArrayList(), exports, processed)
    }

    override fun compile(node: NodeProcessedUse, compiler: Compiler, ctx: CompilationContext) {
        injectModules(node, compiler, ctx)
        node.exports.forEach { NCDefault.compile(it, compiler, ctx) }
        NCDefault.compile(node, compiler, ctx)
    }

    override fun compileVal(node: NodeProcessedUse, compiler: Compiler, ctx: CompilationContext): Variable {
        injectModules(node, compiler, ctx)
        node.exports.forEach { NCDefault.compile(it, compiler, ctx) }
        return NCDefault.compileVal(node, compiler, ctx)
    }

    /**
     * Загружает модули в глобальный контекст.
     */
    private fun injectModules(node: NodeProcessedUse, compiler: Compiler, ctx: CompilationContext) {
        node.names.forEach{ Module.getOrThrow(it).load(compiler, ctx) }
        node.processed.forEach { compiler.compile(it, ctx) }
    }
}