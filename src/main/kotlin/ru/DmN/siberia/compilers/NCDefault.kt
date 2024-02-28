package ru.DmN.siberia.compilers

import ru.DmN.siberia.Compiler
import ru.DmN.siberia.ast.INodesList
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.utils.Variable

/**
 * Компилятор нод с под-нодами.
 */
object NCDefault : INodeCompiler<Node> {
    override fun compile(node: Node, compiler: Compiler, ctx: CompilationContext) {
        node as INodesList
        node.nodes.forEach { compiler.compile(it, ctx) }
    }

    override fun compileVal(node: Node, compiler: Compiler, ctx: CompilationContext): Variable {
        node as INodesList
        node.nodes.dropLast(1).forEach { compiler.compile(it, ctx) }
        return compiler.compileVal(node.nodes.last(), ctx)
    }
}