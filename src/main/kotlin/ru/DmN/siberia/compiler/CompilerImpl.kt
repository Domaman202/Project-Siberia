package ru.DmN.siberia.compiler

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compiler.utils.CompilingStage
import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.utils.IPlatform
import ru.DmN.siberia.utils.Variable
import ru.DmN.siberia.utils.ctx.IContextKey
import ru.DmN.siberia.utils.stage.StageManager
import ru.DmN.siberia.utils.stage.StupidStageManager
import ru.DmN.siberia.utils.vtype.TypesProvider

/**
 * Стандартная реализация компилятора.
 */
open class CompilerImpl(override val mp: ModulesProvider, override val tp: TypesProvider) : Compiler() {
    override val stageManager: StageManager<CompilingStage> = StupidStageManager.of(CompilingStage.UNKNOWN)
    override val contexts: MutableMap<IContextKey, Any?> = HashMap()
    override val finalizers: MutableList<(String) -> Unit> = ArrayList()

    override fun compile(node: Node, ctx: CompilationContext) {
        if (node.info.type.compilable) {
            get(ctx, node).compile(node, this, ctx)
        }
    }

    override fun compileVal(node: Node, ctx: CompilationContext): Variable =
        if (node.info.type.compilable)
            get(ctx, node).compileVal(node, this, ctx)
        else throw UnsupportedOperationException()

    @Suppress("UNCHECKED_CAST")
    override fun get(ctx: CompilationContext, node: Node): INodeCompiler<Node> {
        val platform = ctx.platform
        val type = node.info.type
        ctx.loadedModules.forEach { it -> it.compilers[ctx.platform]?.get(type)?.let { return it as INodeCompiler<Node> } }
        if (platform != IPlatform.UNIVERSAL)
            ctx.loadedModules.forEach { it -> it.compilers[IPlatform.UNIVERSAL]?.get(type)?.let { return it as INodeCompiler<Node> } }
        throw RuntimeException("Compiler for \"$type\" not founded!")
    }
}