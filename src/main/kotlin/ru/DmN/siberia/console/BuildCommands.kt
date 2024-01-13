package ru.DmN.siberia.console

import org.objectweb.asm.ClassWriter
import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.Compiler
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Processor
import ru.DmN.siberia.Unparser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.console.ctx.isModule
import ru.DmN.siberia.console.ctx.module
import ru.DmN.siberia.console.utils.Argument
import ru.DmN.siberia.console.utils.ArgumentType
import ru.DmN.siberia.console.utils.Command
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.Platforms
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.module
import ru.DmN.siberia.processor.utils.with
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.TypesProvider
import java.io.File
import java.io.FileOutputStream
import java.io.PrintStream
import java.net.URLClassLoader

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
        "Компилирует модуль в java байт-код.",
        emptyList(),
        BuildCommands::moduleCtxAvailable,
        BuildCommands::moduleCompile
    )

    val MODULE_RUN = Command(
        "module-run",
        "mr",
        "Модуль",
        "Запуск модуля",
        "Запускает модуль.",
        emptyList(),
        BuildCommands::moduleCtxAvailable,
        BuildCommands::moduleRun
    )

    val MODULE_RUN_TEST = Command(
        "module-run-test",
        "mrt",
        "Модуль",
        "Запуск теста модуля",
        "Запускает тест модуля.",
        listOf(
            Argument(
                "index",
                "Номер",
                ArgumentType.INT,
                "Номер теста модуля.",
                "Введите номер теста"
            )
        ),
        BuildCommands::moduleCtxAvailable,
        BuildCommands::moduleRunTest
    )

    @JvmStatic
    fun moduleRunTest(console: Console, vararg  args: Any?) {
        val index = args[0] as Int
        //
        console.println("Запуск...")
        try {
            console.println(Class.forName("Test$index", true, URLClassLoader(arrayOf(File("dump").toURL()))).getMethod("test").invoke(null))
            console.println("Запуск окончен успешно!")
        } catch (t: Throwable) {
            console.println("Запуск окончен с ошибками:")
            t.printStackTrace(console.print)
        }
    }

    @JvmStatic
    fun moduleRun(console: Console, vararg  args: Any?) {
        console.println("Запуск...")
        try {
            console.println(Class.forName("App", true, URLClassLoader(arrayOf(File("dump").toURL()))).getMethod("main").invoke(null))
            console.println("Запуск окончен успешно!")
        } catch (t: Throwable) {
            console.println("Запуск окончен с ошибками:")
            t.printStackTrace(console.print)
        }
    }

    @JvmStatic
    fun moduleCompile(console: Console, vararg args: Any?) {
        console.println("Компиляция...")
        try {
            val pair = processModule(console)
            val compiler = Compiler(pair.first)
            val cctx = CompilationContext.base()
            pair.second.forEach { compiler.compile(it, cctx) }
            compiler.stageManager.runAll()
            compiler.finalizers.forEach { it.value.run() }
            File("dump").mkdir()
            compiler.classes.values.forEach {
                if (it.name.contains('/'))
                    File("dump/${it.name.substring(0, it.name.lastIndexOf('/'))}").mkdirs()
                FileOutputStream("dump/${it.name}.class").use { stream ->
                    val writer = ClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS)
                    it.accept(writer)
                    val b = writer.toByteArray()
                    stream.write(b)
                }
            }
            console.println("Компиляция окончена успешно!")
        } catch (t: Throwable) {
            console.println("Компиляция окончена с ошибками:")
            t.printStackTrace(console.print)
        }
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
            console.println(" |> Исходные файлы: $files")
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
        val module = Module[name].let {
            if (it?.init != true)
                Parser(Module.getModuleFile(name)).parseNode(ParsingContext.of(StdModule))
            (it ?: Module.getOrThrow(name))
        }
        console.module = module
        module.init()
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
        val pctx = ProcessingContext.base().with(Platforms.JAVA).apply { this.module = module }
        module.load(processor, pctx, ValType.NO_VALUE)
        module.nodes.forEach { it ->
            processor.process(it.copy(), pctx, ValType.NO_VALUE)?.let {
                processed += it
            }
        }
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