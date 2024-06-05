package ru.DmN.siberia.utils.vtype

import ru.DmN.siberia.ast.Node

/**
 * Абстрактный виртуальный метод.
 */
abstract class VirtualMethod {
    /**
     * Класс, которому принадлежит поле.
     */
    abstract val declaringClass: VirtualType

    /**
     * Имя.
     */
    abstract val name: String

    /**
     * Возвращаемый тип.
     */
    abstract val rettype: VirtualType

    /**
     * Generic имя возвращаемого типа
     */
    abstract val retgen: String?

    /**
     * Типы аргументов.
     */
    abstract val argsc: List<VirtualType>

    /**
     * Имена аргументов.
     */
    abstract val argsn: List<String>

    /**
     * Generic имена аргументов.
     */
    abstract val argsg: List<String?>

    /**
     * Модификаторы метода.
     */
    abstract val modifiers: MethodModifiers

    /**
     * Расширяемый тип, если таковой имеется, в противном случае null.
     */
    abstract val extension: VirtualType?

    /**
     * Тело метода, если метод является встраиваемым, в противном случае null.
     */
    abstract val inline: Node?

    override fun equals(other: Any?): Boolean =
        other is VirtualMethod && other.hashCode() == hashCode()

    override fun hashCode(): Int =
        (name.hashCode() * 31 + argsc.hashCode()) * 31 + declaringClass.hashCode()

    /**
     * Простая реализация виртуального метода.
     */
    class Impl(
        override var declaringClass: VirtualType,
        //
        override var name: String,
        //
        override var rettype: VirtualType,
        override val retgen: String?,
        //
        override var argsc: List<VirtualType>,
        override var argsn: List<String>,
        override val argsg: List<String?>,
        //
        override var modifiers: MethodModifiers,
        override var extension: VirtualType?,
        override var inline: Node?,
    ) : VirtualMethod()
}