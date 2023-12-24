package ru.DmN.siberia

import org.objectweb.asm.tree.ClassNode
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compiler.utils.CompilingStage
import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.processor.utils.Platform
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.utils.StupidStageManager
import ru.DmN.siberia.utils.TypesProvider
import ru.DmN.siberia.utils.Variable

/**
 * Компилятор.
 */
class Compiler(
    /**
     * Провайдер типов.
     */
    val tp: TypesProvider
) {
    /**
     * Менеджер стадий компиляции.
     */
    val stageManager = StupidStageManager.of(CompilingStage.UNKNOWN)

    /**
     * Глобальные контексты.
     */
    val contexts: MutableMap<String, Any?> = HashMap()

    /**
     * Список классов.
     */
    val classes: MutableMap<String, ClassNode> = HashMap()

    /**
     * Задачи для конечного этапа компиляции.
     */
    val finalizers: MutableMap<String, Runnable> = HashMap()

    /**
     * Компилирует ноду без значения.
     */
    fun compile(node: Node, ctx: CompilationContext) {
        if (node.info.type.compilable) {
            get(ctx, node).compile(node, this, ctx)
        }
    }

    /**
     * Компилирует ноду со значением.
     */
    fun compileVal(node: Node, ctx: CompilationContext): Variable =
        if (node.info.type.compilable)
            get(ctx, node).compileVal(node, this, ctx)
        else throw UnsupportedOperationException()

    /**
     * Возвращает компилятор нод.
     */
    fun get(ctx: CompilationContext, node: Node): INodeCompiler<Node> {
        val platform = ctx.platform
        val type = node.info.type
        ctx.loadedModules.forEach { it -> it.compilers[ctx.platform]?.get(type)?.let { return it as INodeCompiler<Node> } }
        if (platform != Platform.UNIVERSAL)
            ctx.loadedModules.forEach { it -> it.compilers[Platform.UNIVERSAL]?.get(type)?.let { return it as INodeCompiler<Node> } }
        throw RuntimeException("Compiler for \"$type\" not founded!")
    }
}