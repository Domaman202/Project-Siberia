package ru.DmN.siberia.console.commands

import ru.DmN.siberia.console.BuildCommands
import ru.DmN.siberia.console.BuildCommands.processModule
import ru.DmN.siberia.console.Console
import ru.DmN.siberia.console.ctx.isModule
import ru.DmN.siberia.console.utils.Command
import ru.DmN.siberia.processor.utils.mp
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.unparser.UnparserImpl
import ru.DmN.siberia.unparser.ctx.UnparsingContext
import ru.DmN.siberia.utils.exception.BaseException
import java.io.File
import java.io.FileOutputStream

object ModuleUnparse : Command(
    "module-unparse",
    "mu",
    "Модуль",
    "Де-парсинг модуля",
    "Де-парсит модуль.",
    emptyList()
) {
    override fun available(console: Console): Boolean =
        console.isModule

    override fun builderAvailable(flags: Map<Any?, Any?>): Boolean =
        flags["module"] != null

    override fun action(console: Console, vararg args: Any?) {
        val mp = console.mp
        val platform = console.platform
        //
        console.println("Де-парсинг...")
        try {
            val nodes = processModule(console)
            File("dump").mkdir()
            FileOutputStream("dump/unparse.pht").use { out ->
                val unparser = UnparserImpl(mp, 1024 * 1024)
                val uctx = UnparsingContext.base().apply { this.platform = platform }
                nodes.forEach { unparser.unparse(it, uctx, 0) }
                out.write(unparser.out.toString().toByteArray())
            }
            console.println("Де-парсинг окончен успешно!")
        } catch (e: BaseException) {
            console.println("Де-парсинг окончен с ошибками:\n${e.print(BuildCommands::provider)}")
            console.stop(1)
        } catch (t: Throwable) {
            console.println("Де-парсинг окончен с ошибками:")
            t.printStackTrace(console.print)
            console.stop(1)
        }
    }
}