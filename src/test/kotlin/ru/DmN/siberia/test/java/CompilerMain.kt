package ru.DmN.siberia.test.java

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.ClassWriter.COMPUTE_FRAMES
import org.objectweb.asm.ClassWriter.COMPUTE_MAXS
import ru.DmN.siberia.Compiler
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Processor
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.processor.utils.Platform
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.with
import ru.DmN.siberia.utils.TypesProvider
import ru.DmN.siberia.utils.readBytes
import java.io.File
import java.io.FileOutputStream
import java.net.URLClassLoader

object CompilerMain {
    @JvmStatic
    fun main(args: Array<String>) {
        val source = Parser(String(CompilerMain::class.java.getResourceAsStream("/test.pht")!!.readBytes())).parseNode(ParsingContext.base())!!
        val processor = Processor(TypesProvider.JAVA)
        val pctx = ProcessingContext.base().with(Platform.JAVA)
        val processed = processor.process(source, pctx, ValType.NO_VALUE)!!
        processor.stageManager.runAll()
        val compiler = Compiler(TypesProvider.JAVA)
        val cctx = CompilationContext.base()
        compiler.compile(processed, cctx)
        compiler.stageManager.runAll()
        compiler.finalizers.forEach { it.value.run() }
        compiler.classes.values.forEach {
            if (it.name.contains('/'))
                File("dump/${it.name.substring(0, it.name.lastIndexOf('/'))}").mkdirs()
            FileOutputStream("dump/${it.name}.class").use { stream ->
                val writer = ClassWriter(COMPUTE_FRAMES + COMPUTE_MAXS)
                it.accept(writer)
                val b = writer.toByteArray()
                stream.write(b)
            }
        }
        // test
        println(Class.forName("App", true, URLClassLoader(arrayOf(File("dump").toURL()))).getMethod("main").invoke(null))
//        println(Class.forName("App").run { getMethod("main").invoke(getField("INSTANCE").get(null)) })
    }

    init {
        File("dump").mkdir()
    }
}