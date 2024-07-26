package ru.DmN.siberia.ast

import ru.DmN.siberia.utils.indent
import ru.DmN.siberia.utils.meta.IMetadataKey
import ru.DmN.siberia.utils.node.INodeInfo

/**
 * Абстракция AST ноды.
 */
interface Node {
    /**
     * Информация о ноде.
     */
    val info: INodeInfo

    /**
     * Копирует ноду.
     * Перегрузите это если в вашей ноде есть что изменять.
     */
    fun copy(): Node =
        this

    /**
     * Устанавливает метаданные ноды.
     *
     * Чтобы удалить метаданные в параметр value передайте null.
     */
    fun setMetadata(key: IMetadataKey, value: Any?): Unit =
        Unit

    /**
     * Вызывает всех слушателей обновления метаданных ноды.
     */
    fun visitMetadata(key: IMetadataKey, value: Any?): Unit =
        Unit

    /**
     * Получает метаданные моды.
     *
     * В случае отсутствия метаданных возвращает null.
     */
    fun getMetadata(key: IMetadataKey): Any? =
        null

    /**
     * Печатает ноду.
     *
     * @param indent Отступ.
     */
    fun print(builder: StringBuilder, indent: Int): StringBuilder =
        this.printShort(builder, indent)

    /**
     * Печатает ноду в кратком варианте.
     *
     * @param indent Отступ.
     */
    fun printShort(builder: StringBuilder, indent: Int): StringBuilder =
        builder.indent(indent).append('[').append(info.type).append(']')

    /**
     * Печатает ноду.
     *
     * @param short Режим краткого вывода.
     */
    fun print(short: Boolean = true): String =
        if (short)
            printShort(StringBuilder(), 0).toString()
        else print(StringBuilder(), 0).toString()
}