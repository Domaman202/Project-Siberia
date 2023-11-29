package ru.DmN.siberia

import org.objectweb.asm.ClassWriter
import ru.DmN.pht.std.module.StdModule
import ru.DmN.pht.std.module.ast.NodeModule
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.processor.utils.Platform
import ru.DmN.siberia.processor.utils.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.with
import ru.DmN.siberia.utils.Module
import ru.DmN.siberia.utils.TypesProvider
import java.io.File
import java.io.FileOutputStream
import java.net.URLClassLoader

object Console {
    private var CompileWithoutFrameCompute = false

    @JvmStatic
    fun main(args: Array<String>) {
        while (true) {
            print("""
                Список действий
                0. Вывод информации о программе
                1. Вывод информации о модуле
                2. Компиляция модуля
                3. Компиляция и запуск модуля
                4. Изменить параметры
                
                Выберите действие
                > 
                """.trimIndent()
            )
            when (val action0 = readln().toInt().apply { println() }) {
                0 -> {
                    println("""
                        Проект: Сибирь
                        Версия: 1.0.1
                        Автор:  DomamaN202
                        """.trimIndent()
                    )
                }

                1 -> {
                    print("Введите название модуля: ")
                    printModuleInfo(readln().let {
                        val module = Module[it]
                        if (module?.init != true)
                            Parser(Module.getModuleFile(it)).parseNode(ParsingContext.of(StdModule))
                        (module ?: Module.getOrThrow(it))
                    })
                }

                2 -> {
                    print("Введите расположение модуля: ")
                    compileModule(readln())
                }

                3 -> {
                    print("Введите расположение модуля: ")
                    compileModule(readln())
                    println()
                    println(Class.forName("App", true, URLClassLoader(arrayOf(File("dump").toURL()))).getMethod("main").invoke(null))
                }

                4 -> {
                    while (true) {
                        print("""
                            Список действий
                            0. Выйти
                            1. CompileWithoutFrameCompute (${CompileWithoutFrameCompute})
                            
                            Выберите действие
                            > 
                            """.trimIndent()
                        )

                        when (val action1 = readln().toInt().apply { println() }) {
                            0 -> break
                            1 -> CompileWithoutFrameCompute = !CompileWithoutFrameCompute
                            else -> println("Неизвестное действие №$action1.")
                        }
                    }
                }

                else -> println("Неизвестное действие №$action0.")
            }
            println()
        }
    }

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

    private val Module.dependencies: String
        get() {
            val list = ArrayList<Pair<Int, String>>()
            printDeps(this, list, 0)
            return list.map { "${"'".repeat(it.first)}${it.second}" }.toString()
        }

    private fun printDeps(module: Module, list: MutableList<Pair<Int, String>>, i: Int) {
        list.addAll(module.deps.map { Pair(i, it) })
        module.deps.forEach { printDeps(Module.getOrThrow(it), list, i + 1) }
    }

    private fun compileModule(dir: String) {
        val module = (Parser(Module.getModuleFile(dir)).parseNode(ParsingContext.of(StdModule)) as NodeModule).module
        printModuleInfo(module)
        val processed = ArrayList<Node>()
        val processor = Processor(TypesProvider.JAVA)
        val pctx = ProcessingContext.base().with(Platform.JAVA)
        processed += module.load(processor, pctx, ValType.NO_VALUE)!!
        processor.tasks.forEach {
            pctx.stage.set(it.key)
            it.value.forEach { it() }
        }
        val compiler = Compiler(TypesProvider.JAVA)
        val cctx = CompilationContext.base()
        processed.forEach { compiler.compile(it, cctx) }
        compiler.tasks.forEach {
            cctx.stage.set(it.key)
            it.value.forEach { it() }
        }
        File("dump").mkdir()
        compiler.classes.values.forEach {
            if (it.name.contains('/'))
                File("dump/${it.name.substring(0, it.name.lastIndexOf('/'))}").mkdirs()
            FileOutputStream("dump/${it.name}.class").use { stream ->
                val writer = ClassWriter(
                    if (CompileWithoutFrameCompute) 0
                    else ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS
                )
                it.accept(writer)
                val b = writer.toByteArray()
                stream.write(b)
            }
        }
    }
}