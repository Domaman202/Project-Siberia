package ru.DmN.siberia.utils

/**
 * Модификаторы поля.
 */
data class FieldModifiers(
    /**
     * Поле неизменяемо?
     */
    var isFinal: Boolean,

    /**
     * Статическое ли поле?
     */
    var isStatic: Boolean,

    /**
     * Является ли поле instance-ом enum-а?
     */
    var isEnum: Boolean
)