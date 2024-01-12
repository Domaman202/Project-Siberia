package ru.DmN.siberia.console.ctx

import ru.DmN.siberia.console.Console
import ru.DmN.siberia.ctx.IContextCollection
import ru.DmN.siberia.utils.Module

val IContextCollection<Console>.isModule
    get() = contexts[ContextKeys.MODULE] != null

var IContextCollection<Console>.module: Module
    set(value) { contexts[ContextKeys.MODULE] = value }
    get() = contexts[ContextKeys.MODULE] as Module