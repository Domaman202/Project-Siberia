package ru.DmN.siberia.compilers

import ru.DmN.siberia.Compiler
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.Variable

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
        node.names.forEach { Module.getOrThrow(it).load(compiler, context) }
        node.processed.forEach { compiler.compile(it, context) }
        node.exports.forEach { NCDefault.compile(it, compiler, ctx) }
        return context
    }
}