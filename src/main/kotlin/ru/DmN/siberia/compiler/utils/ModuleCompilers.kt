package ru.DmN.siberia.compiler.utils

import ru.DmN.pht.module.utils.Module
import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.utils.IPlatform
import ru.DmN.siberia.utils.node.INodeType

/**
 * Под-модуль реализующий функционал для определённой платформы.
 *
 * @param name Имя модуля.
 * @param platform Платформа.
 */
abstract class ModuleCompilers(name: String, platform: IPlatform) : Module(name) {
    init {
        this.platform = platform
    }

    fun add(type: INodeType, compiler: INodeCompiler<*>) {
        add(platform!!, type, compiler)
    }
}