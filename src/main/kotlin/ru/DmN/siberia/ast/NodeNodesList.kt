package ru.DmN.siberia.ast

import ru.DmN.siberia.utils.indent
import ru.DmN.siberia.utils.mapMutable
import ru.DmN.siberia.utils.node.INodeInfo

/**
 * Нода с под-нодами.
 */
open class NodeNodesList(info: INodeInfo, override val nodes: MutableList<Node> = mutableListOf()) : BaseNode(info), INodesList {
    override fun copy(): NodeNodesList =
        NodeNodesList(info, copyNodes())

    /**
     * Копирует под-ноды
     */
    fun copyNodes(): MutableList<Node> =
        nodes.mapMutable { it.copy() }
}