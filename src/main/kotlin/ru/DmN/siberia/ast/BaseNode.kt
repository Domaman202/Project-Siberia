package ru.DmN.siberia.ast

import ru.DmN.siberia.utils.node.INodeInfo

/**
 * Базовая AST нода.
 */
open class BaseNode(override val info: INodeInfo) : Node {
    override fun equals(other: Any?): Boolean =
        other is BaseNode && other.info == info

    override fun hashCode(): Int =
        info.hashCode()
}