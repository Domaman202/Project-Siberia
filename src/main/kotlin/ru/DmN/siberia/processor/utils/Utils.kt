package ru.DmN.siberia.processor.utils

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.utils.IContextCollection
import java.util.*

fun nodeProgn(line: Int, nodes: MutableList<Node>) =
    NodeNodesList(Token.operation(line, "progn"), nodes)

fun <T : IContextCollection<T>> T.with(ctx: Platform) =
    this.with("siberia/platform", ctx)


fun IContextCollection<*>.isExports() =
    contexts.containsKey("pht/exports")

/**
 * Платформа
 */
var IContextCollection<*>.platform
    set(value) { this.contexts["siberia/platform"] = value }
    get() = this.contexts["siberia/platform"] as Platform

var IContextCollection<*>.exports
    set(value) { contexts["pht/exports"] = value }
    get() = contexts["pht/exports"] as Stack<MutableList<NodeNodesList>>