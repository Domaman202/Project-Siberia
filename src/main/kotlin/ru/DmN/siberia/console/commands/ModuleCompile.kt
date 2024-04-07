package ru.DmN.siberia.console.commands

import ru.DmN.siberia.compiler.CompilerImpl
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.console.BuildCommands.processModule
import ru.DmN.siberia.console.Console
import ru.DmN.siberia.console.ctx.isModule
import ru.DmN.siberia.console.utils.Command
import ru.DmN.siberia.processor.utils.mp
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.processor.utils.tp
import java.io.File

object ModuleCompile : Command(
    "module-compile",
    "mc",
    "Модуль",
    "Компиляция модуля",
    "Компилирует модуль.",
    emptyList()
) {
    override fun available(console: Console): Boolean =
        console.isModule

    override fun builderAvailable(flags: Map<Any?, Any?>): Boolean =
        flags["module"] != null

    override fun action(console: Console, vararg args: Any?) {
        val mp = console.mp
        val tp = console.tp
        val platform = console.platform
        //
        console.println("Компиляция...")
        try {
            val nodes = processModule(console)
            val compiler = CompilerImpl(mp, tp)
            val ctx = CompilationContext.base().apply { this.platform = platform }
            nodes.forEach { compiler.compile(it, ctx) }
            compiler.stageManager.runAll()
            File("dump").mkdir()
            compiler.finalizers.forEach { it("dump") }
            console.println("Компиляция окончена успешно!")
        } catch (t: Throwable) {
            console.println("Компиляция окончена с ошибками:")
            t.printStackTrace(console.print)
        }
    }
}