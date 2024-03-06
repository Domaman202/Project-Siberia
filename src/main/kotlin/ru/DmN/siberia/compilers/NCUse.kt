package ru.DmN.siberia.compilers

import ru.DmN.siberia.compiler.Compiler
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.utils.Variable

object NCUse : INodeCompiler<NodeProcessedUse> {
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
        node.names.forEach{ compiler.mp.getOrThrow(it).load(compiler, ctx) }
        node.processed.forEach { compiler.compile(it, ctx) }
    }
}