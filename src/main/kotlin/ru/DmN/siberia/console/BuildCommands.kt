package ru.DmN.siberia.console

import ru.DmN.pht.module.utils.getOrLoadModule
import ru.DmN.siberia.Compiler
import ru.DmN.siberia.Processor
import ru.DmN.siberia.Unparser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.console.ctx.isModule
import ru.DmN.siberia.console.ctx.module
import ru.DmN.siberia.console.utils.Argument
import ru.DmN.siberia.console.utils.ArgumentType
import ru.DmN.siberia.console.utils.Command
import ru.DmN.siberia.parsers.NPUseCtx.getModules
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.*
import ru.DmN.siberia.processors.NRProgn
import ru.DmN.siberia.processors.NRUse
import ru.DmN.siberia.processors.NRUseCtx
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.TypesProvider
import java.io.File
import java.io.FileOutputStream

object BuildCommands {
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
        BaseCommands::alviseAvailable,
        BuildCommands::moduleSelect
    )

    val MODULE_PRINT = Command(
        "module-print",
        "mp",
        "Модуль",
        "Информация о модуле",
        "Выводит информацию о модуле.",
        emptyList(),
        BuildCommands::moduleCtxAvailable,
        BuildCommands::modulePrint
    )


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
        BuildCommands::moduleCtxAvailable,
        BuildCommands::platformSelect
    )

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
    fun moduleCompile(console: Console, vararg args: Any?) {
        console.println("Компиляция...")
        try {
            val pair = processModule(console)
            val compiler = Compiler(pair.first)
            val cctx = CompilationContext.base()
            pair.second.forEach { compiler.compile(it, cctx) }
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
    fun platformSelect(console: Console, vararg args: Any?) {
        val name = (args[0] as String).toUpperCase()
        //
        val platform = Platforms.entries.find { it.name == name }
        if (platform == null) {
            console.println("Платформа '$name' не найдена!")
            return
        }
        console.platform = platform
        //
        console.println("Выбрана платформа '$name'.")
    }

    @JvmStatic
    fun moduleUnparse(console: Console, vararg args: Any?) {
        console.println("Де-парсинг...")
        try {
            val pair = processModule(console)
            FileOutputStream("dump/unparse.pht").use { out ->
                val unparser = Unparser(1024 * 1024)
                val uctx = UnparsingContext.base()
                pair.second.forEach { unparser.unparse(it, uctx, 0) }
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
        if (validateModule(console, name))
            return
        val module = getOrLoadModule(name)
        console.module = module
        module.init(Platforms.UNIVERSAL)
        console.println("Выбран модуль '${module.name}'.")
    }

    @JvmStatic
    fun moduleCtxAvailable(console: Console): Boolean =
        console.isModule

    @JvmStatic
    private fun processModule(console: Console): Pair<TypesProvider, List<Node>> {
        val module = console.module
        console.println("Обработка...")
        //
        val tp = TypesProvider.java()
        val processed = ArrayList<Node>()
        val processor = Processor(tp)
        NRUseCtx.injectModules(mutableListOf(module.name), processed, processed, processor, ProcessingContext.base().with(console.platform).apply { this.module = module })
        processor.stageManager.runAll()
        //
        console.println("Обработка успешна завершена!")
        return Pair(tp, processed)
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