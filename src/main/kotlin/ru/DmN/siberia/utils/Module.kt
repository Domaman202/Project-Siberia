package ru.DmN.siberia.utils

import ru.DmN.siberia.Compiler
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Processor
import ru.DmN.siberia.Unparser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.node.INodeType
import ru.DmN.siberia.node.NodeInfoImpl
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.parsers.NPUseCtx
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.Platform
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.module
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.unparsers.INodeUnparser
import java.io.File
import java.io.FileNotFoundException
import java.util.*

open class Module(val name: String) {
    var init: Boolean = false
    var version: String = "0.0.0"
    var author: String = "unknown"
    var deps: List<String> = ArrayList()
    var uses: List<String> = ArrayList()
    var files: List<String> = ArrayList()
    val parsers: MutableMap<Regex, INodeParser> = HashMap()
    val unparsers: MutableMap<INodeType, INodeUnparser<*>> = HashMap()
    val processors: MutableMap<INodeType, INodeProcessor<*>> = HashMap()
    val compilers: MutableMap<Platform, MutableMap<INodeType, INodeCompiler<*>>> = EnumMap(Platform::class.java)
    val nodes: MutableList<Node> = ArrayList()
    val exports: MutableList<NodeNodesList> = ArrayList()

    init {
        initParsers()
        initUnparsers()
        initProcessors()
        initCompilers()
    }

    open fun initParsers() =
        Unit

    open fun initUnparsers() =
        Unit

    open fun initProcessors() =
        Unit

    open fun initCompilers() =
        Unit

    fun init() {
        if (!init) {
            init = true
            files.forEach {
                val parser = Parser(getModuleFile(it))
                val pctx = ParsingContext.base().apply { this.module = this@Module }
                nodes.add(
                    NPUseCtx.parse(uses, parser, pctx) { context ->
                        NodeUse(
                            NodeInfoImpl(NodeTypes.USE_CTX, null, null),
                            uses,
                            mutableListOf(parser.parseNode(context)!!),
                        )
                    },
                )
            }
        }
    }

    open fun load(parser: Parser, ctx: ParsingContext) {
        if (!ctx.loadedModules.contains(this)) {
            ctx.loadedModules.add(0, this)
        }
    }

    open fun clear(parser: Parser, ctx: ParsingContext) = Unit

    open fun unload(parser: Parser, ctx: ParsingContext) {
        if (ctx.loadedModules.contains(this)) {
            ctx.loadedModules.remove(this)
        }
    }

    open fun load(unparser: Unparser, ctx: UnparsingContext) {
        if (!ctx.loadedModules.contains(this)) {
            ctx.loadedModules.add(0, this)
        }
    }

    open fun load(processor: Processor, ctx: ProcessingContext, mode: ValType): Boolean =
        if (!ctx.loadedModules.contains(this)) {
            ctx.loadedModules.add(0, this)
            true
        } else false

    open fun load(compiler: Compiler, ctx: CompilationContext): Variable? {
        if (!ctx.loadedModules.contains(this)) {
            ctx.loadedModules.add(0, this)
        }
        return null
    }

    fun getModuleFile(file: String): String {
        val stream = Module::class.java.getResourceAsStream("/$name/$file")
        if (stream == null) {
            val f = File("$name/$file")
            if (f.isFile)
                return String(f.readBytes())
            throw FileNotFoundException("/$name/$file")
        }
        return String(stream.readBytes())
    }

    fun add(regex: Regex, parser: INodeParser) {
        parsers[regex] = parser
    }

    fun add(type: INodeType, unparser: INodeUnparser<*>) {
        unparsers[type] = unparser
    }

    fun add(type: INodeType, processor: INodeProcessor<*>) {
        processors[type] = processor
    }

    fun add(platform: Platform, type: INodeType, compiler: INodeCompiler<*>) {
        compilers.getOrPut(platform) { HashMap() }[type] = compiler
    }

    override fun toString(): String =
        "Module[$name]"

    companion object {
        private val MODULES: MutableList<Module> = ArrayList()

        fun getOrPut(name: String, put: () -> Module): Module =
            get(name) ?: put().apply { MODULES.add(this) }

        fun getOrThrow(name: String) =
            get(name) ?: throw RuntimeException("Module '$name' not founded!")

        operator fun get(name: String): Module? {
            for (i in 0 until MODULES.size) {
                val module = MODULES[i]
                if (module.name == name) {
                    return module
                }
            }

            return null
        }

        fun getModuleFile(name: String): String {
            val stream = Module::class.java.getResourceAsStream("/$name/module.pht")
            if (stream == null) {
                val file = File("$name/module.pht")
                if (file.isFile)
                    return String(file.readBytes())
                throw FileNotFoundException("/$name/module.pht")
            }
            return String(stream.readBytes())
        }

        fun String.toRegularExpr(): Regex =
            this.replace(".", "\\.")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("\\", "\\\\")
                .replace("?", "\\?")
                .replace("*", "\\*")
                .replace("+", "\\+")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .toRegex()
    }
}