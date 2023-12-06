package ru.DmN.siberia.utils

import ru.DmN.siberia.Compiler
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Processor
import ru.DmN.siberia.Unparser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeParsedUse
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.module
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.unparsers.INodeUnparser
import ru.DmN.siberia.ups.NUPUseCtx
import java.io.File
import java.io.FileNotFoundException
import ru.DmN.siberia.compilers.INodeCompiler as JavaNodeCompiler

open class Module(val name: String) {
    var init: Boolean = false
    var version: String = "0.0.0"
    var author: String = "unknown"
    val deps: MutableList<String> = ArrayList()
    val uses: MutableList<String> = ArrayList()
    val files: MutableList<String> = ArrayList()
    val parsers: MutableMap<Regex, INodeParser> = HashMap()
    val unparsers: MutableMap<Regex, INodeUnparser<*>> = HashMap()
    val processors: MutableMap<Regex, INodeProcessor<*>> = HashMap()
    val javaCompilers: MutableMap<Regex, JavaNodeCompiler<*>> = HashMap()
    val nodes: MutableList<Node> = ArrayList()
    val exports: MutableList<NodeNodesList> = ArrayList()

    fun init() {
        if (!init) {
            init = true
            files.forEach {
                val parser = Parser(getModuleFile(it))
                val pctx = ParsingContext.base().apply { this.module = this@Module }
                nodes.add(
                    NUPUseCtx.parse(uses, parser, pctx) { context ->
                        NodeParsedUse(
                            Token.operation(-1, "use-ctx"),
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

    fun add(name: String, compiler: JavaNodeCompiler<*>) =
        add(name.toRegularExpr(), compiler)

    fun add(name: Regex, compiler: JavaNodeCompiler<*>) {
        javaCompilers[name] = compiler
    }

    fun add(name: String, parser: INodeParser? = null, unparser: INodeUnparser<*>? = null, processor: INodeProcessor<*>? = null) =
        add(name.toRegularExpr(), parser, unparser, processor)

    fun add(name: Regex, parser: INodeParser? = null, unparser: INodeUnparser<*>? = null, processor: INodeProcessor<*>? = null) {
        parser      ?.let { parsers     [name] = it }
        unparser    ?.let { unparsers   [name] = it }
        processor   ?.let { processors  [name] = it }
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