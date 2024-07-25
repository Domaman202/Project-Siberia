package ru.DmN.siberia.compiler

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compiler.utils.CompilingStage
import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.utils.IPlatform.UNIVERSAL
import ru.DmN.siberia.utils.SubMap
import ru.DmN.siberia.utils.Variable
import ru.DmN.siberia.utils.ctx.IContextKey
import ru.DmN.siberia.utils.exception.compilationCatcher
import ru.DmN.siberia.utils.stage.DefaultStageManager
import ru.DmN.siberia.utils.stage.StageManager
import ru.DmN.siberia.utils.vtype.TypesProvider

/**
 * Стандартная реализация компилятора.
 */
open class CompilerImpl(
    override val mp: ModulesProvider,
    override val tp: TypesProvider,
    override val contexts: MutableMap<IContextKey, Any?> = HashMap()
) : Compiler() {
    override val stageManager: StageManager = DefaultStageManager(CompilingStage.UNKNOWN, CompilingStage.entries.toMutableList())
    override val finalizers: MutableList<(String) -> Unit> = ArrayList()

    override fun compile(node: Node, ctx: CompilationContext) = compilationCatcher(node) {
        if (node.info.type.compilable) {
            get(ctx, node).compile(node, this, ctx)
        }
    }

    override fun compileVal(node: Node, ctx: CompilationContext): Variable = compilationCatcher(node) {
        if (node.info.type.compilable)
            get(ctx, node).compileVal(node, this, ctx)
        else throw UnsupportedOperationException()
    }

    @Suppress("UNCHECKED_CAST")
    override fun get(ctx: CompilationContext, node: Node): INodeCompiler<Node> {
        val platform = ctx.platform
        val type = node.info.type
        ctx.loadedModules.forEach { it -> it.compilers[ctx.platform]?.get(type)?.let { return it as INodeCompiler<Node> } }
        if (platform != UNIVERSAL)
            ctx.loadedModules.forEach { it -> it.compilers[UNIVERSAL]?.get(type)?.let { return it as INodeCompiler<Node> } }
        throw RuntimeException("Compiler for \"$type\" not founded!")
    }

    override fun subCompiler(ctx: CompilationContext?): Compiler =
        CompilerImpl(mp, tp, SubMap(contexts))
}