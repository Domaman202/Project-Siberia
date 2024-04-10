package ru.DmN.siberia.utils.meta

import java.util.*

/**
 * Хранилище метаданных.
 */
class MetadataContainer(
    /**
     * Метаданные.
     */
    val metadata: MutableMap<IMetadataKey, Any?> = TreeMap(),

    /**
     * Наблюдатели обновлений.
     */
    val visitors: MutableList<IMetadataVisitor> = ArrayList()
) {
    /**
     * Компирование метаданных без наблюдателей.
     */
    fun copy() =
        MetadataContainer(TreeMap(metadata))

    /**
     * Установка метаданных.
     *
     * @param key Ключ.
     * @param value Значение.
     */
    operator fun set(key: IMetadataKey, value: Any?) {
        visit(key, value)
        if (value == null)
            metadata.remove(key)
        else metadata[key] = value
    }

    /**
     * Обзвон наблюдателей обновлений.
     *
     * @param key Ключ метаданных.
     * @param value Значение метаданных.
     */
    fun visit(key: IMetadataKey, value: Any?) {
        visitors.forEach { it.onUpdate(key, value) }
    }

    /**
     * Получение метаданных.
     *
     * @param key Ключ.
     * @return Метаданные, null - если метаданные отсутствуют.
     */
    operator fun get(key: IMetadataKey): Any? =
        metadata[key]

    /**
     * Добавление наблюдателя обновлений.
     */
    fun addVisitor(visitor: IMetadataVisitor) {
        visitors += visitor
    }

    /**
     * Удаление наблюдателя обновлений.
     */
    fun removeVisitor(visitor: IMetadataVisitor) {
        visitors -= visitor
    }
}
