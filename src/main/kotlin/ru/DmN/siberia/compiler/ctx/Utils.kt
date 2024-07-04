@file:Suppress("UNCHECKED_CAST")
package ru.DmN.siberia.compiler.ctx

import ru.DmN.pht.module.utils.Module
import ru.DmN.siberia.utils.ctx.ContextKeys.COMPILED_MODULES
import ru.DmN.siberia.utils.ctx.ContextKeys.SPLIT_MODULE_BUILD
import ru.DmN.siberia.utils.ctx.IContextKey

/**
 * Список собранных модулей.
 */
var MutableMap<IContextKey, Any?>.compiledModules
    set(value) { this[COMPILED_MODULES] = value }
    get() = this[COMPILED_MODULES] as MutableList<Module>?

/**
 * Режим раздельной сборки модулей.
 */
var MutableMap<IContextKey, Any?>.splitModuleBuild
    set(value) { this[SPLIT_MODULE_BUILD] = value }
    get() = this[SPLIT_MODULE_BUILD] as Boolean? ?: false