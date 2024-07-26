package ru.DmN.siberia.console.commands

import ru.DmN.siberia.console.BuildCommands
import ru.DmN.siberia.console.BuildCommands.processModule
import ru.DmN.siberia.console.Console
import ru.DmN.siberia.console.ctx.isModule
import ru.DmN.siberia.console.utils.Argument
import ru.DmN.siberia.console.utils.ArgumentType
import ru.DmN.siberia.console.utils.Command
import ru.DmN.siberia.utils.exception.BaseException
import java.io.File
import java.io.FileOutputStream

object ModulePrint : Command(
    "module-print",
    "mp",
    "Модуль",
    "Печать AST модуля",
    "Печатает AST деревья каждого исходного файла модуля.",
    listOf(
        Argument(
            "mode",
            "Режим",
            ArgumentType.STRING,
            "Режим печати.",
            "Введите режим печати (short/long)"
        )
    )
) {
    override fun available(console: Console): Boolean =
        console.isModule

    override fun builderAvailable(flags: Map<Any?, Any?>): Boolean =
        flags["module"] != null

    override fun action(console: Console, vararg args: Any?) {
        val mode = args[0] == "short"
        //
        console.println("Печать...")
        try {
            File("dump").mkdir()
            val nodes = processModule(console)
            FileOutputStream(if (mode) "dump/print.short.pht" else "dump/print.pht").use { out ->
                val sb = StringBuilder()
                if (mode)
                    nodes.forEach { it.printShort(sb, 0) }
                else nodes.forEach { it.print(sb, 0) }
                out.write(sb.toString().toByteArray())
            }
            console.println("Печать окончена успешно!")
        } catch (e: BaseException) {
            console.println("Печать окончена с ошибками:\n${e.print(BuildCommands::provider)}")
        } catch (t: Throwable) {
            console.println("Печать окончена с ошибками:")
            t.printStackTrace(console.print)
            console.stop(1)
        }
    }
}