package ru.DmN.siberia.node

/**
 * Информация о ноде
 */
interface INodeInfo {
    /**
     * Тип ноды.
     */
    val type: INodeType

    /**
     * Файл из которого получена нода.
     */
    val file: String?

    /**
     * Строка на которой была получена нода.
     */
    val line: Int?

    /**
     * Возвращает информацию с изменённым типом.
     */
    fun withType(type: INodeType): INodeInfo
}