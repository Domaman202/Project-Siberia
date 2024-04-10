package ru.DmN.siberia.utils.node

/**
 * Информация о токене.
 */
interface ITokenInfo {
    /**
     * Файл.
     */
    val file: String

    /**
     * Строка.
     */
    val line: Int

    /**
     * Первый символ.
     */
    val symbol: Int

    /**
     * Длина.
     */
    val length: Int

    /**
     * Копирование информации.
     */
    fun copy() = this
}