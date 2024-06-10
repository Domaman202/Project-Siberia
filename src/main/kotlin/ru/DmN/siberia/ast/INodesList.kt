package ru.DmN.siberia.ast

/**
 * Интерфейс описывающий ноду, имеющую под-ноды.
 */
interface INodesList : Node {
    /**
     * Под-ноды.
     */
    val nodes: MutableList<Node>

    override fun copy(): INodesList =
        this
}