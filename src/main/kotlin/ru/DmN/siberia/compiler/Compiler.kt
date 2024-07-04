package ru.DmN.siberia.compiler

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compiler.utils.CompilingStage
import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.utils.Variable
import ru.DmN.siberia.utils.ctx.IContextKey
import ru.DmN.siberia.utils.stage.StageManager
import ru.DmN.siberia.utils.vtype.TypesProvider

/**
 * Абстракция компилятора.
 */
abstract class Compiler {
    /**
     * Поставщик модулей.
     */
    abstract val mp: ModulesProvider

    /**
     * Поставщик типов.
     */
    abstract val tp: TypesProvider

    /**
     * Менеджер стадий компиляции.
     */
    abstract val stageManager: StageManager<CompilingStage>

    /**
     * Глобальные контексты.
     */
    abstract val contexts: MutableMap<IContextKey, Any?>

    /**
     * Задачи для конечного этапа компиляции.
     */
    abstract val finalizers: MutableList<(String) -> Unit>


    /**
     * Компилирует ноду без значения.
     */
    abstract fun compile(node: Node, ctx: CompilationContext)

    /**
     * Компилирует ноду со значением.
     */
    abstract fun compileVal(node: Node, ctx: CompilationContext): Variable

    /**
     * Возвращает компилятор нод.
     */
    abstract fun get(ctx: CompilationContext, node: Node): INodeCompiler<Node>

    /**
     * Создаёт новый компилятор, наследует глобальный контекст.
     *
     * @param ctx Контекст компиляции.
     */
    abstract fun subCompiler(ctx: CompilationContext?): Compiler
}