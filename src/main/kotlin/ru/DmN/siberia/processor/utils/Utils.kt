package ru.DmN.siberia.processor.utils

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.utils.IContextCollection
import ru.DmN.siberia.utils.Module

fun nodeProgn(line: Int, nodes: MutableList<Node>) =
    NodeNodesList(Token.operation(line, "progn"), nodes)

fun <T : IContextCollection<T>> T.with(ctx: Platform) =
    this.with("siberia/platform", ctx)


fun IContextCollection<*>.isExports() =
    contexts.containsKey("pht/exports")

/**
 * Текущий модуль
 */
var IContextCollection<*>.module
    set(value) { contexts["pht/module"] = value }
    get() = contexts["pht/module"] as Module

/**
 * Текущий модуль (если существует)
 */
var IContextCollection<*>.moduleOrNull
    set(value) { contexts["pht/module"] = value }
    get() = contexts["pht/module"] as Module?

/**
 * Платформа
 */
var IContextCollection<*>.platform
    set(value) { this.contexts["siberia/platform"] = value }
    get() = this.contexts["siberia/platform"] as Platform