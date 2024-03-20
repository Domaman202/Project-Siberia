package ru.DmN.siberia.utils.vtype

import ru.DmN.siberia.ast.Node
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.TypeVariable

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

    companion object {
        /**
         * Создаёт новый метод.
         * Использует typeOf метод для взятия новых типов по имени.
         */
        fun of(typeOf: (name: String) -> VirtualType, ctor: Constructor<*>): VirtualMethod =
            of(typeOf(ctor.declaringClass.name), ctor)

        /**
         * Создаёт новый метод.
         * Использует typeOf метод для взятия новых типов по имени.
         */
        fun of(typeOf: (name: String) -> VirtualType, method: Method): VirtualMethod =
            of(typeOf(method.declaringClass.name), method)

        /**
         * Создаёт новый метод.
         */
        fun of(ctor: Constructor<*>): VirtualMethod =
            of(VirtualType.ofKlass(ctor.declaringClass), ctor)

        /**
         * Создаёт новый метод.
         */
        fun of(method: Method): VirtualMethod =
            of(VirtualType.ofKlass(method.declaringClass), method)

        /**
         * Создаёт новый метод.
         */
        private fun of(declaringClass: VirtualType, method: Constructor<*>): VirtualMethod {
            val argsc = ArrayList<VirtualType>()
            val argsn = ArrayList<String>()
            val argsg = ArrayList<String?>()
            method.parameters.forEach {
                argsc += VirtualType.ofKlass(it.type)
                argsn += it.name
                argsg += if (it.parameterizedType is TypeVariable<*>) (it.parameterizedType as TypeVariable<*>).name else null
            }
            return Impl(
                declaringClass,
                "<init>",
                VirtualType.VOID,
                null,
                argsc,
                argsn,
                argsg,
                MethodModifiers(
                    varargs = method.isVarArgs,
                    static = Modifier.isStatic(method.modifiers),
                    abstract = method.declaringClass.isInterface,
                    final = Modifier.isFinal(method.modifiers)
                ),
                null,
                null
            )
        }

        /**
         * Создаёт новый метод.
         */
        private fun of(declaringClass: VirtualType, method: Method): VirtualMethod {
            val argsc = ArrayList<VirtualType>()
            val argsn = ArrayList<String>()
            val argsg = ArrayList<String?>()
            method.parameters.forEach {
                argsc += VirtualType.ofKlass(it.type)
                argsn += it.name
                argsg += if (it.parameterizedType is TypeVariable<*>) (it.parameterizedType as TypeVariable<*>).name else null
            }
            return Impl(
                declaringClass,
                method.name,
                VirtualType.ofKlass(method.returnType),
                if (method.genericReturnType is TypeVariable<*>) (method.genericReturnType as TypeVariable<*>).name else null,
                argsc,
                argsn,
                argsg,
                MethodModifiers(
                    varargs = method.isVarArgs,
                    static = Modifier.isStatic(method.modifiers),
                    abstract = Modifier.isAbstract(method.modifiers),
                    final = Modifier.isFinal(method.modifiers)
                ),
                null,
                null
            )
        }
    }

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