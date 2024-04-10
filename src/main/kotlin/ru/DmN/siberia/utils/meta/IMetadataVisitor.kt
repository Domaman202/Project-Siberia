package ru.DmN.siberia.utils.meta

/**
 * Наблюдатель обновлений метаданных.
 */
fun interface IMetadataVisitor {
    /**
     * Событие обновления метаданных.
     *
     * @param key Ключ.
     * @param value Значение.
     */
    fun onUpdate(key: IMetadataKey, value: Any?)
}