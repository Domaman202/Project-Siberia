package ru.DmN.siberia.console

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeProcessedUse
import ru.DmN.siberia.console.commands.*
import ru.DmN.siberia.console.ctx.module
import ru.DmN.siberia.processor.ProcessorImpl
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.*
import ru.DmN.siberia.processors.NRUseCtx.injectModules
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

object BuildCommands {
    @JvmStatic
    val ALL_COMMANDS = listOf(PlatformSelect, ModuleSelect, ModuleInfo, ModulePrint, ModuleUnparse, ModuleCompile, ModuleCompileSplit)

    @JvmStatic
    fun processModule(console: Console): List<Node> {
        val mp = console.mp
        val tp = console.tp
        val module = console.module
        val platform = console.platform
        //
        console.println("Обработка...")
        //
        val processed = ArrayList<Node>()
        val processor = ProcessorImpl(mp, tp)
        val list = ArrayList<NodeProcessedUse.ProcessedData>()
        mp.injectModules(mutableListOf(module.name), list, processor, ProcessingContext.base().with(platform).apply { this.module = module })
        list.forEach { processed += it.processed; processed += it.exports }
        processor.sm.runAll()
        //
        console.println("Обработка успешна завершена!")
        return processed
    }

    @JvmStatic
    fun provider(path: String): InputStream {
        val file = File(path)
        if (file.isFile)
            return FileInputStream(file)
        return Thread.currentThread().contextClassLoader.getResourceAsStream(path)!!
    }
}