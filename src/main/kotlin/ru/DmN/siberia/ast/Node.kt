package ru.DmN.siberia.ast

import ru.DmN.siberia.utils.indent
import ru.DmN.siberia.utils.meta.IMetadataKey
import ru.DmN.siberia.utils.node.INodeInfo

/**
 * Абстракция AST ноды
 */
abstract class Node {
    /**
     * Информация о ноде.
     */
    abstract val info: INodeInfo

    /**
     * Копирует ноду.
     * Перегрузите это если в вашей ноде есть что изменять.
     */
    open fun copy(): Node =
        this

    /**
     * Устанавливает метаданные ноды.
     *
     * Чтобы удалить метаданные в параметр value передайте null.
     */
    open fun setMetadata(key: IMetadataKey, value: Any?): Unit =
        throw UnsupportedOperationException()

    /**
     * Вызывает всех слушателей обновления метаданных ноды.
     */
    open fun visitMetadata(key: IMetadataKey, value: Any?): Unit =
        throw UnsupportedOperationException()

    /**
     * Получает метаданные моды.
     *
     * В случае отсутствия метаданных возвращает null.
     */
    open fun getMetadata(key: IMetadataKey): Any? =
        throw UnsupportedOperationException()

    /**
     * Печатает ноду.
     *
     * @param indent отступ.
     * @param short Режим краткого вывода.
     */
    open fun print(builder: StringBuilder, indent: Int, short: Boolean): StringBuilder =
        builder.indent(indent).append('[').append(info.type).append(']')

    /**
     * Печатает ноду.
     *
     * @param short Режим краткого вывода.
     */
    fun print(short: Boolean = true): String =
        print(StringBuilder(), 0, short).toString()

    override fun equals(other: Any?): Boolean =
        other === this || (other is Node && other.info == info)

    override fun hashCode(): Int =
        info.hashCode()
}