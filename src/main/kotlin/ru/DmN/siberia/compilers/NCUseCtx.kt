package ru.DmN.siberia.compilers

import ru.DmN.pht.module.utils.Module
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.compiler.Compiler
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compiler.ctx.compiledModules
import ru.DmN.siberia.compiler.ctx.splitModuleBuild
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.processor.utils.with
import ru.DmN.siberia.utils.Variable
import ru.DmN.siberia.utils.invokeAll

object NCUseCtx : INodeCompiler<NodeProcessedUse> {
    override fun compile(node: NodeProcessedUse, compiler: Compiler, ctx: CompilationContext) =
        NCDefault.compile(node, compiler, injectModules(node, compiler, ctx))

    override fun compileVal(node: NodeProcessedUse, compiler: Compiler, ctx: CompilationContext): Variable =
        NCDefault.compileVal(node, compiler, injectModules(node, compiler, ctx))

    /**
     * Загружает модули в локальный контекст.
     *
     * @return Локальный контекст с модулями.
     */
    private fun injectModules(node: NodeProcessedUse, compiler: Compiler, ctx: CompilationContext): CompilationContext {
        val context = ctx.subCtx()
        if (compiler.contexts.splitModuleBuild) {
            val platform = ctx.platform
            val compiledModules = compiler.contexts.compiledModules ?: ArrayList<Module>().apply { compiler.contexts.compiledModules = this }
            node.data.forEach { it ->
                it.module.load(compiler, context)
                if (!compiledModules.contains(it.module)) {
                    compiledModules += it.module
                    val tmpContext = CompilationContext.base().with(it.module).apply { this.platform = platform }
                    val tmpCompiler = compiler.subCompiler(tmpContext)
                    it.processed.forEach { tmpCompiler.compile(it, tmpContext) }
                    tmpCompiler.stageManager.runAll()
                    compiler.finalizers += tmpCompiler.finalizers::invokeAll
                }
                it.exports.forEach { NCDefault.compile(it, compiler, context) }
            }
        } else {
            val compiledModules = compiler.contexts.compiledModules ?: ArrayList<Module>().apply { compiler.contexts.compiledModules = this }
            node.data.forEach { it ->
                it.module.load(compiler, context)
                if (!compiledModules.contains(it.module)) {
                    compiledModules += it.module
                    val tmpContext = context.with(it.module)
                    it.processed.forEach { compiler.compile(it, tmpContext) }
                }
                it.exports.forEach { NCDefault.compile(it, compiler, context) }
            }
        }
        return context
    }
}