package ru.DmN.siberia.ast

/**
 * Интерфейс описывающий ноду, имеющую под-ноды.
 */
interface INodesList {
    /**
     * Под-ноды.
     */
    val nodes: MutableList<Node>
}