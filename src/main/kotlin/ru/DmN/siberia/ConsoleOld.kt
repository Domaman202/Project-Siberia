package ru.DmN.siberia

import org.objectweb.asm.ClassWriter
import ru.DmN.pht.std.module.StdModule
import ru.DmN.pht.std.module.ast.NodeModule
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.processor.utils.Platform
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.with
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.TypesProvider
import java.io.File
import java.io.FileOutputStream
import java.net.URLClassLoader

object ConsoleOld : Console() {
    @JvmStatic
    fun Console.initHelp() {
        this.actions.add(Triple("Помощь", "Выводит информацию о действиях.", Runnable {
            print("Выберите действие по которому хотите получить помощь:\n> ")
            val index = readln().toInt()
            val action = actions.getOrNull(index)
            println()
            if (action == null)
                println("Действие №$index не найдено!\n")
            else {
                println("""
                    Номер:    $index
                    Действие: ${action.first}
                    Описание: ${action.second}
                """.trimIndent())
            }
        }))
    }

    @JvmStatic
    fun Console.initProgramInfo() {
        this.actions.add(Triple("О программе", "Выводит информацию о программе.", Runnable {
            println("""
                Проект: Сибирь
                Версия: 1.3.0
                Авторы: DomamaN202, AirBtw
            """.trimIndent())
        }))
    }

    @JvmStatic
    fun Console.initModuleInfo() {
        this.actions.add(Triple("О модуле", "Выводит информацию о модуле.", Runnable {
            print("Введите название модуля: ")
            printModuleInfo(readln().let {
                val module = Module[it]
                if (module?.init != true)
                    Parser(Module.getModuleFile(it)).parseNode(ParsingContext.of(StdModule))
                (module ?: Module.getOrThrow(it))
            })
        }))
    }

    @JvmStatic
    fun Console.initCompileModule() {
        this.actions.add(Triple("Собрать", "Компилирует модуль.", Runnable {
            print("Введите расположение модуля: ")
            compileModule(readln())
        }))
    }

    @JvmStatic
    fun Console.initCompileAndRunModule() {
        this.actions.add(Triple("Собрать & Запустить", "Компилирует и запускает модуль.", Runnable {
            print("Введите расположение модуля: ")
            compileModule(readln())
            println()
            println(Class.forName("App", true, URLClassLoader(arrayOf(File("dump").toURL()))).getMethod("main").invoke(null))
        }))
    }

    @JvmStatic
    fun main(args: Array<String>) {
        initHelp()
        initProgramInfo()
        initModuleInfo()
        initCompileModule()
        initCompileAndRunModule()
        run()
    }

    @JvmStatic
    private fun printModuleInfo(module: Module) {
        println("""
            Имя:           ${module.name}
            Версия:        ${module.version}
            Автор:         ${module.author}
            Зависимости:   ${module.dependencies}
            Использование: ${module.uses}
            Файлы:         ${module.files}
            Класс:         ${module.javaClass}
            """.trimIndent()
        )
    }

    @JvmStatic
    private val Module.dependencies: String
        get() {
            val list = ArrayList<Pair<Int, String>>()
            printDeps(this, list, 0)
            return list.map { "${"'".repeat(it.first)}${it.second}" }.toString()
        }

    @JvmStatic
    private fun printDeps(module: Module, list: MutableList<Pair<Int, String>>, i: Int) {
        list.addAll(module.deps.map { Pair(i, it) })
        module.deps.forEach { printDeps(Module.getOrThrow(it), list, i + 1) }
    }

    @JvmStatic
    private fun compileModule(dir: String) {
        val module = (Parser(Module.getModuleFile(dir)).parseNode(ParsingContext.of(StdModule)) as NodeModule).module
        printModuleInfo(module)
        val processed = ArrayList<Node>()
        val processor = Processor(TypesProvider.JAVA)
        val pctx = ProcessingContext.base().with(Platform.JAVA)
        processed += module.load(processor, pctx, ValType.NO_VALUE)!!
        processor.stageManager.runAll()
        val compiler = Compiler(TypesProvider.JAVA)
        val cctx = CompilationContext.base()
        processed.forEach { compiler.compile(it, cctx) }
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
    }
}