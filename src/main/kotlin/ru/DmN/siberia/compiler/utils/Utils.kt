package ru.DmN.siberia.compiler.utils

import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.ctx.ContextKeys

/**
 * Возвращает контекст,
 *
 * С целевой (и/или текущей) версией java классов.
 *
 * @param ctx Версия java классов.
 */
fun CompilationContext.withJCV(ctx: Int) =
    this.with(ContextKeys.JCV, ctx)

/**
 * Целевая (и/или текущая) версия java классов.
 */
var CompilationContext.javaClassVersion
    set(value) { this.contexts[ContextKeys.JCV] = value }
    get() = this.contexts[ContextKeys.JCV] as Int