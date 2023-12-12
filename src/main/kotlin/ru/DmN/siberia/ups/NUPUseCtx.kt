package ru.DmN.siberia.ups

import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.*
import ru.DmN.siberia.ast.*
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compilers.NCDefault
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.lexer.Token.DefaultType.OPERATION
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.NPDefault
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ProcessingStage
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.module
import ru.DmN.siberia.processors.NRDefault
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.unparsers.NUDefault
import ru.DmN.siberia.utils.INUPC
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.Variable
import ru.DmN.siberia.utils.text

/**
 * Универсальный обработчик инструкции использования в контексте.
 */
object NUPUseCtx : INUPC<NodeUse, NodeUse, NodeProcessedUse> {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val names = ArrayList<String>()
        var tk = parser.nextToken()!!
        while (tk.type == OPERATION) {
            names.add(tk.text!!)
            tk = parser.nextToken()!!
        }
        parser.tokens.push(tk)
        return parse(names, parser, ctx) { context ->
            NPDefault.parse(parser, context) { NodeUse(token, names, it) }
        }
    }

    fun parse(names: List<String>, parser: Parser, ctx: ParsingContext, parse: (context: ParsingContext) -> Node): Node {
        val context = ctx.subCtx()
        loadModules(names, parser, context)
        val node = parse(context)
        context.loadedModules.filter { names.contains(it.name) }.forEach { it.clear(parser, context) }
        return node
    }

    override fun unparse(node: NodeUse, unparser: Unparser, ctx: UnparsingContext, indent: Int) {
        loadModules(node.names, unparser, ctx)
        unparser.out.apply {
            append('(').append(node.text).append(' ')
            node.names.forEachIndexed { i, it ->
                append(it)
                if (node.names.size + 1 < i) {
                    append(' ')
                }
            }
            NUDefault.unparseNodes(node, unparser, ctx, indent)
            append(')')
        }
    }

    override fun process(node: NodeUse, processor: Processor, ctx: ProcessingContext, mode: ValType): Node {
        val exports = ArrayList<NodeNodesList>()
        val processed = ArrayList<Node>()
        processor.stageManager.pushTask(ProcessingStage.MODULE_POST_INIT) {
            val context = ctx.subCtx()
            injectModules(node, processor, context, ValType.NO_VALUE, processed).forEach { it ->
                context.module = ctx.module
                it.exports.forEach {
                    exports += NRDefault.process(it.copy(), processor, context, ValType.NO_VALUE)
                }
            }
            NRDefault.process(node, processor, context, mode)
        }
        return NodeProcessedUse(node.token, node.names, node.nodes, exports, processed)
    }

    override fun compile(node: NodeProcessedUse, compiler: Compiler, ctx: CompilationContext) {
        val context = injectModules(node, compiler, ctx)
        node.exports.forEach { NCDefault.compile(it, compiler, context) }
        NCDefault.compile(node, compiler, context)
    }

    override fun compileVal(node: NodeProcessedUse, compiler: Compiler, ctx: CompilationContext): Variable {
        val context = injectModules(node, compiler, ctx)
        node.exports.forEach { NCDefault.compile(it, compiler, ctx) }
        return NCDefault.compileVal(node, compiler, context)
    }

    /**
     * Загружает модули в контекст парсинга.
     *
     * @param names Имена модулей.
     */
    fun loadModules(names: List<String>, parser: Parser, ctx: ParsingContext) {
        names.forEach {
            val module = Module[it]
            if (module?.init != true)
                Parser(Module.getModuleFile(it)).parseNode(ParsingContext.of(Siberia, StdModule))
            (module ?: Module.getOrThrow(it)).load(parser, ctx)
        }
    }

    /**
     * Загружает модули в контекст де-парсинга.
     *
     * @param names Имена модулей.
     */
    fun loadModules(names: List<String>, unparser: Unparser, ctx: UnparsingContext) {
        names.forEach { name ->
            val module = Module[name]
            if (module?.init != true)
                Parser(Module.getModuleFile(name)).parseNode(ParsingContext.of(Siberia, StdModule))
            (module ?: Module.getOrThrow(name)).load(unparser, ctx)
        }
    }

    /**
     * Загружает модули в контекст процессинга из ноды.
     *
     * @param processed Список в который будут помещены обработанные ноды из модулей.
     */
    fun injectModules(node: NodeUse, processor: Processor, ctx: ProcessingContext, mode: ValType, processed: MutableList<Node>): List<Module> =
        node.names
            .asSequence()
            .map { Module.getOrThrow(it) }
            .onEachIndexed { i, it ->
                if (it.load(processor, ctx, if (i + 1 < node.names.size) ValType.NO_VALUE else mode)) {
                    ctx.module = it
                    it.nodes.forEach { it1 ->
                        processor.process(it1.copy(), ctx, ValType.NO_VALUE)?.let {
                            processed += it
                        }
                    }
                }
            }
            .toList()

    /**
     * Загружает модули в локальный контекст.
     *
     * @return Локальный контекст с модулями.
     */
    private fun injectModules(node: NodeProcessedUse, compiler: Compiler, ctx: CompilationContext): CompilationContext {
        val context = ctx.subCtx()
        node.names.forEach { Module.getOrThrow(it).load(compiler, context) }
        node.processed.forEach { compiler.compile(it, context) }
        return context
    }
}