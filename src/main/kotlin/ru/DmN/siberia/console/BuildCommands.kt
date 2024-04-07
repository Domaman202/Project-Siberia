@file:Suppress("UNUSED_PARAMETER")
package ru.DmN.siberia.console

import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.pht.module.utils.getOrLoadModule
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.CompilerImpl
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.console.ctx.isModule
import ru.DmN.siberia.console.ctx.module
import ru.DmN.siberia.console.utils.Argument
import ru.DmN.siberia.console.utils.ArgumentType
import ru.DmN.siberia.console.utils.Command
import ru.DmN.siberia.processor.ProcessorImpl
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.*
import ru.DmN.siberia.processors.NRUseCtx.injectModules
import ru.DmN.siberia.unparser.UnparserImpl
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.utils.IPlatform
import ru.DmN.siberia.utils.vtype.TypesProvider
import java.io.File
import java.io.FileOutputStream

object BuildCommands {
    @JvmStatic
    val PLATFORM_SELECT = Command(
        "platform",
        "p",
        "Модуль",
        "Выбрать платформу",
        "Выбирает целевую платформу для дальнейшей работы.",
        listOf(
            Argument(
                "name",
                "Название",
                ArgumentType.STRING,
                "Название платформы.",
                "Введите название платформы"
            )
        ),
        BaseCommands::alviseAvailable,
        BuildCommands::platformSelect
    )

    @JvmStatic
    val MODULE_SELECT = Command(
        "module",
        "m",
        "Модуль",
        "Выбрать модуль",
        "Выбирает модуль для дальнейшей работы.",
        listOf(
            Argument(
                "name",
                "Имя",
                ArgumentType.STRING,
                "Имя модуля.",
                "Введите имя модуля"
            )
        ),
        BuildCommands::platformCtxAvailable,
        BuildCommands::moduleSelect
    )

    @JvmStatic
    val MODULE_INFO = Command(
        "module-info",
        "mi",
        "Модуль",
        "Информация о модуле",
        "Выводит информацию о модуле.",
        emptyList(),
        BuildCommands::moduleCtxAvailable,
        BuildCommands::moduleInfo
    )


    @JvmStatic
    val MODULE_PRINT = Command(
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
        ),
        BuildCommands::moduleCtxAvailable,
        BuildCommands::modulePrint
    )

    @JvmStatic
    val MODULE_UNPARSE = Command(
        "module-unparse",
        "mu",
        "Модуль",
        "Де-парсинг модуля",
        "Де-парсит модуль.",
        emptyList(),
        BuildCommands::moduleCtxAvailable,
        BuildCommands::moduleUnparse
    )

    @JvmStatic
    val MODULE_COMPILE = Command(
        "module-compile",
        "mc",
        "Модуль",
        "Компиляция модуля",
        "Компилирует модуль.",
        emptyList(),
        BuildCommands::moduleCtxAvailable,
        BuildCommands::moduleCompile
    )

    @JvmStatic
    val ALL_COMMANDS = listOf(PLATFORM_SELECT, MODULE_SELECT, MODULE_INFO, MODULE_PRINT, MODULE_UNPARSE, MODULE_COMPILE)

    @JvmStatic
    fun moduleCompile(console: Console, vararg args: Any?) {
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

    @JvmStatic
    fun moduleUnparse(console: Console, vararg args: Any?) {
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
        } catch (t: Throwable) {
            console.println("Де-парсинг окончен с ошибками:")
            t.printStackTrace(console.print)
        }
    }

    @JvmStatic
    fun modulePrint(console: Console, vararg args: Any?) {
        val mode = args[0] == "short"
        //
        console.println("Печать...")
        try {
            File("dump").mkdir()
            val nodes = processModule(console)
            FileOutputStream("dump/print.pht").use { out ->
                val sb = StringBuilder()
                nodes.forEach { it.print(sb, 0, mode) }
                out.write(sb.toString().toByteArray())
            }
            console.println("Печать окончена успешно!")
        } catch (t: Throwable) {
            console.println("Печать окончена с ошибками:")
            t.printStackTrace(console.print)
        }
    }

    @JvmStatic
    fun moduleInfo(console: Console, vararg args: Any?) {
        console.module.run {
            console.clear()
            console.println("[T]")
            console.println(" |> Имя: $name")
            console.println(" |> Версия: $version")
            console.println(" |> Автор(ы): $author")
            console.println(" |> Зависимости: $deps")
            console.println(" |> Использует модули: $uses")
            console.println(" |> Исходный код: $sources")
            console.println(" |> Ресурсные: $resources")
            console.println(" |> Инициализация: ${if (init) "инициализирован" else "не инициализирован"}")
            console.println("[V]")
        }
    }

    @JvmStatic
    fun moduleSelect(console: Console, vararg args: Any?) {
        val name = args[0] as String
        //
        val mp = console.mp
        val platform = console.platform
        //
        if (validateModule(console, name))
            return
        val module = mp.getOrLoadModule(name, platform)
        console.module = module
        module.init(platform, mp)
        console.println("Выбран модуль '${module.name}'.")
    }

    @JvmStatic
    fun platformSelect(console: Console, vararg args: Any?) {
        val name = (args[0] as String).uppercase()
        //
        val platform = IPlatform.PLATFORMS.find { it.name == name }
        if (platform == null) {
            console.println("Платформа '$name' не найдена!")
            return
        }
        //
        console.platform = platform
        console.tp = TypesProvider.of(platform)
        console.mp = ModulesProvider.of(platform)
        //
        console.println("Выбрана платформа '$name'.")
    }

    @JvmStatic
    fun moduleCtxAvailable(console: Console): Boolean =
        console.isModule

    @JvmStatic
    fun platformCtxAvailable(console: Console): Boolean =
        console.isPlatform

    @JvmStatic
    private fun processModule(console: Console): List<Node> {
        val mp = console.mp
        val tp = console.tp
        val module = console.module
        val platform = console.platform
        //
        console.println("Обработка...")
        //
        val processed = ArrayList<Node>()
        val processor = ProcessorImpl(mp, tp)
        mp.injectModules(mutableListOf(module.name), processed, processed, processor, ProcessingContext.base().with(platform).apply { this.module = module })
        processor.stageManager.runAll()
        //
        console.println("Обработка успешна завершена!")
        return processed
    }

    @JvmStatic
    private fun validateModule(console: Console, name: String): Boolean {
        if (Module::class.java.getResourceAsStream("/$name/module.pht") == null) {
            val file = File("$name/module.pht")
            if (!file.exists() || !file.isFile) {
                console.println("Не удалось найти модуль '${name}'.")
                return true
            }
        }
        return false
    }
}