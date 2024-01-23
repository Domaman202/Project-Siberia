package ru.DmN.siberia.compiler.utils

import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.node.INodeType
import ru.DmN.siberia.processor.utils.Platforms
import ru.DmN.siberia.utils.Module

/**
 * Под-модуль реализующий функционал для определённой платформы.
 *
 * @param name Имя модуля.
 * @param platform Платформа.
 */
abstract class ModuleCompilers(name: String, private val platform: Platforms) : Module(name) {
    fun add(type: INodeType, compiler: INodeCompiler<*>) {
        add(platform, type, compiler)
    }
}