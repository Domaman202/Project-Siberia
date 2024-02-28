package ru.DmN.siberia.compiler.utils

import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.utils.node.INodeType
import ru.DmN.siberia.utils.IPlatform
import ru.DmN.pht.module.utils.Module

/**
 * Под-модуль реализующий функционал для определённой платформы.
 *
 * @param name Имя модуля.
 * @param platform Платформа.
 */
abstract class ModuleCompilers(name: String, private val platform: IPlatform) : Module(name) {
    fun add(type: INodeType, compiler: INodeCompiler<*>) {
        add(platform, type, compiler)
    }
}