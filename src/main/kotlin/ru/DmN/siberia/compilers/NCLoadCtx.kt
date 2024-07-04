package ru.DmN.siberia.compilers

import ru.DmN.siberia.ast.NodeProcessedLoad
import ru.DmN.siberia.compiler.Compiler
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.utils.Variable

object NCLoadCtx : INodeCompiler<NodeProcessedLoad> {
    override fun compile(node: NodeProcessedLoad, compiler: Compiler, ctx: CompilationContext) =
        NCDefault.compile(node, compiler, injectModules(node, ctx))

    override fun compileVal(node: NodeProcessedLoad, compiler: Compiler, ctx: CompilationContext): Variable =
        NCDefault.compileVal(node, compiler, injectModules(node, ctx))


    @Suppress("NOTHING_TO_INLINE")
    private inline fun injectModules(node: NodeProcessedLoad, ctx: CompilationContext): CompilationContext {
        val context = ctx.subCtx()
        context.loadedModules += node.modules
        return context
    }
}