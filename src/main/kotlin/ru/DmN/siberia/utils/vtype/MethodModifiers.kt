package ru.DmN.siberia.utils.vtype

/**
 * Модификаторы метода.
 */
data class MethodModifiers(
    /**
     * Метод является расширением?
     */
    var extension: Boolean = false,

    /**
     * Метод абстрактный?
     */
    var abstract: Boolean = false,

    /**
     * Метод является генератором?
     */
    var generator: Boolean = false,

    /**
     * Метод принимает переменное число аргументов?
     */
    var varargs: Boolean = false,

    /**
     * Метод статический?
     */
    var static: Boolean = false,

    /**
     * Метод встраиваемый?
     */
    var inline: Boolean = false,

    /**
     * Метод конечный?
     */
    var final: Boolean = false,

    /**
     * Метод нативный?
     */
    var native: Boolean = false,

    /**
     * Метод является конструктором?
     */
    var ctor: Boolean = false,

    /**
     * Метод синхронизуемый?
     */
    var sync: Boolean = false,

    /**
     * Метод описывает функционал файла или может быть представлен в виде файла?
     */
    var file: Boolean = false
)
