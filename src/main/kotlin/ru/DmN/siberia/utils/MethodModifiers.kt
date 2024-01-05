package ru.DmN.siberia.utils

/**
 * Модификаторы метода.
 */
data class MethodModifiers(
    /**
     * Метод принимает переменное число аргументов?
     */
    var varargs: Boolean = false,

    /**
     * Метод является конструктором?
     */
    var ctor: Boolean = false,

    /**
     * Метод статический?
     */
    var static: Boolean = false,

    /**
     * Метод абстрактный?
     */
    var abstract: Boolean = false,

    /**
     * Метод является расширением?
     */
    var extension: Boolean = false,

    /**
     * Метод конечный?
     */
    var final: Boolean = false
)
