package ru.DmN.siberia.compiler.utils

import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.node.INodeType
import ru.DmN.siberia.processor.utils.Platform
import ru.DmN.siberia.utils.Module

/**
 * Реализуйте для выполнения инициализации компиляторов определённой платформы для вашего модуля.
 *
 * @param module Модуль.
 * @param platform Платформа.
 */
abstract class ModuleCompilers(private val module: Module, private val platform: Platform) {
    private var init = false

    /**
     * Инициализирует компиляторы.
     *
     * @see Module.initCompilers
     */
    @Synchronized
    fun init() {
        if (!init) {
            init = true
            onInitialize()
        }
    }

    protected abstract fun onInitialize()

    fun add(type: INodeType, compiler: INodeCompiler<*>) {
        module.add(platform, type, compiler)
    }
}