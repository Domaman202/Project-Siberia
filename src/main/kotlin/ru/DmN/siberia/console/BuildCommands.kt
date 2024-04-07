package ru.DmN.siberia.console

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.console.commands.*
import ru.DmN.siberia.console.ctx.module
import ru.DmN.siberia.processor.ProcessorImpl
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.*
import ru.DmN.siberia.processors.NRUseCtx.injectModules

object BuildCommands {
    @JvmStatic
    val ALL_COMMANDS = listOf(PlatformSelect, ModuleSelect, ModuleInfo, ModulePrint, ModuleUnparse, ModuleCompile)

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
        mp.injectModules(mutableListOf(module.name), processed, processed, processor, ProcessingContext.base().with(platform).apply { this.module = module })
        processor.stageManager.runAll()
        //
        console.println("Обработка успешна завершена!")
        return processed
    }
}