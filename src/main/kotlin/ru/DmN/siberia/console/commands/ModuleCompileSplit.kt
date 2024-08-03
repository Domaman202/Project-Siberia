package ru.DmN.siberia.console.commands

import ru.DmN.siberia.compiler.CompilerImpl
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compiler.ctx.splitModuleBuild
import ru.DmN.siberia.console.BuildCommands
import ru.DmN.siberia.console.BuildCommands.processModule
import ru.DmN.siberia.console.Console
import ru.DmN.siberia.console.ctx.isModule
import ru.DmN.siberia.console.ctx.module
import ru.DmN.siberia.console.utils.Command
import ru.DmN.siberia.processor.utils.*
import ru.DmN.siberia.utils.exception.BaseException
import ru.DmN.siberia.utils.invokeAll
import java.io.File

object ModuleCompileSplit : Command(
    "module-compile-split",
    "mcs",
    "Модуль",
    "Раздельняа компиляция модуля",
    "Компилирует модуль (подмодули компилируется отдельно).",
    emptyList()
) {
    override fun available(console: Console): Boolean =
        console.isModule

    override fun builderAvailable(flags: Map<Any?, Any?>): Boolean =
        flags["module"] != null

    override fun action(console: Console, vararg args: Any?) {
        val mp = console.mp
        val tp = console.tp
        val module = console.module
        val platform = console.platform
        //
        console.println("Компиляция...")
        try {
            val nodes = processModule(console)
            val compiler = CompilerImpl(mp, tp)
            compiler.contexts.splitModuleBuild = true
            val ctx = CompilationContext.base().with(platform).apply { this.module = module }
            nodes.forEach { compiler.compile(it, ctx) }
            compiler.sm.runAll()
            File("dump").mkdir()
            compiler.finalizers.invokeAll("dump")
            console.println("Компиляция окончена успешно!")
        } catch (e: BaseException) {
            console.println("Компиляция окончена с ошибками:\n${e.print(BuildCommands::provider)}")
            console.stop(1)
        } catch (t: Throwable) {
            console.println("Компиляция окончена с ошибками:")
            t.printStackTrace(console.print)
            console.stop(1)
        }
    }
}