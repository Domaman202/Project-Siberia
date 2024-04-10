package ru.DmN.siberia.utils.node

/**
 * Тип ноды.
 */
interface INodeType {
    /**
     * Операция которая будет указываться при де-парсе
     */
    val operation: String

    /**
     * Нужно ли обрабатывать ноду?
     */
    val processable: Boolean

    /**
     * Нужно ли компилировать ноду?
     */
    val compilable: Boolean
}