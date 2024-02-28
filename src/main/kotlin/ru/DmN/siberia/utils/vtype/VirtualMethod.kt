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

    /**
     * Generic's (Name / Type)
     */
    abstract val generics: Map<String, VirtualType>

    /**
     * Дескриптор аргументов.
     */
    val argsDesc: String
        get() {
            val str = StringBuilder()
            argsc.forEach { str.append(it.desc) }
            return str.toString()
        }

    /**
     * Дескриптор метода.
     */
    val desc: String
        get() = "($argsDesc)${if (name.startsWith("<")) "V" else rettype.desc}"

    /**
     * Сигнатура метода.
     */
    val signature: String?
        get() =
            if (generics.isEmpty())
                null
            else {
                val sb = StringBuilder()
                if (!modifiers.static) {
                    val list = generics.entries.drop(declaringClass.generics.size)
                    if (list.isNotEmpty()) {
                        sb.append('<')
                        list.forEach {
                            sb.append(it.key).append(':').append(it.value.desc)
                        }
                        sb.append('>')
                    }
                }
                sb.append('(')
                argsg.forEach { sb.append('T').append(it).append(';') }
                sb.append(')').append(retgen?.let { "T${retgen};" } ?: rettype.desc).toString()
            }

    override fun equals(other: Any?): Boolean =
        other is VirtualMethod && other.hashCode() == hashCode()

    override fun hashCode(): Int =
        (name.hashCode() * 31 + desc.hashCode()) * 31 + declaringClass.hashCode()

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
            return VirtualMethodImpl(
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
                null,
                declaringClass.generics // todo:
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
            return VirtualMethodImpl(
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
                null,
                declaringClass.generics // todo:
            )
        }
    }

    /**
     * Простая реализация виртуального метода.
     */
    class VirtualMethodImpl(
        override var declaringClass: VirtualType,
        override var name: String,
        override var rettype: VirtualType,
        override val retgen: String?,
        override var argsc: List<VirtualType>,
        override var argsn: List<String>,
        override val argsg: List<String?>,
        override var modifiers: MethodModifiers,
        override var extension: VirtualType?,
        override var inline: Node?,
        override var generics: Map<String, VirtualType>
    ) : VirtualMethod()
}