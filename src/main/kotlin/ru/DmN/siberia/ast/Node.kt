package ru.DmN.siberia.ast

import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.utils.indent

/**
 * Базовая AST нода
 */
open class Node(val info: INodeInfo) {
    /**
     * Копирует ноду.
     * Перегрузите это если в вашей ноде есть что изменять.
     */
    open fun copy(): Node =
        this

    /**
     * Печатает ноду.
     *
     * @param indent отступ
     */
    open fun print(builder: StringBuilder, indent: Int, short: Boolean): StringBuilder =
        builder.indent(indent).append('[').append(info.type).append(']')

    fun print(short: Boolean = true): String =
        print(StringBuilder(), 0, short).toString()
}