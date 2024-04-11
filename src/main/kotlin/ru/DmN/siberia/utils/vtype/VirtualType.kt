package ru.DmN.siberia.utils.vtype

import ru.DmN.siberia.utils.Klass
import ru.DmN.siberia.utils.isPrimitive
import ru.DmN.siberia.utils.klassOf
import java.lang.reflect.Modifier
import java.util.*

/**
 * Абстрактный виртуальный тип.
 */
abstract class VirtualType {
    /**
     * Имя.
     */
    abstract val name: String

    /**
     * Чистое имя (для сравнивания типов без префиксов).
     */
    open val cname: String
        get() = name

    /**
     * Предки.
     */
    abstract val parents: List<VirtualType>

    /**
     * Поля.
     */
    abstract val fields: List<VirtualField>

    /**
     * Методы
     */
    abstract val methods: List<VirtualMethod>

    /**
     * Тип исходного элемента, в случае если тип не произвольный возвращает null.
     */
    abstract val componentType: VirtualType?

    /**
     * Тип это интерфейс?
     */
    abstract val isInterface: Boolean

    /**
     * Тип абстрактный?
     */
    abstract val isAbstract: Boolean

    /**
     * Тип конечный?
     */
    abstract val isFinal: Boolean

    /**
     * Тип описывает функционал файла или может быть представлен в виде файла?
     */
    abstract val isFile: Boolean

    /**
     * Тип является примитивом?
     */
    open val isPrimitive
        get() = name.isPrimitive()
    /**
     * Тип можно получить из целевого?
     */
    open fun isAssignableFrom(target: VirtualType): Boolean =
        if (target.cname == this.cname || parents.any { it.isAssignableFrom(target) })
            true
        else if (isInterface)
            target == ofKlass(Any::class.java)
        else false

    override fun hashCode(): Int =
        name.hashCode()

    override fun equals(other: Any?): Boolean =
        if (other is VirtualType)
            other.name == name
        else false

    override fun toString(): String {
        return "VT($name)"
    }

    companion object {
        private val TYPES: MutableMap<String, VirtualType> = WeakHashMap()
        val VOID    = ofKlass(Void::class.javaPrimitiveType!!)
        val BOOLEAN = ofKlass(Boolean::class.javaPrimitiveType!!)
        val BYTE    = ofKlass(Byte::class.javaPrimitiveType!!)
        val SHORT   = ofKlass(Short::class.javaPrimitiveType!!)
        val CHAR    = ofKlass(Char::class.javaPrimitiveType!!)
        val INT     = ofKlass(Int::class.javaPrimitiveType!!)
        val LONG    = ofKlass(Long::class.javaPrimitiveType!!)
        val FLOAT   = ofKlass(Float::class.javaPrimitiveType!!)
        val DOUBLE  = ofKlass(Double::class.javaPrimitiveType!!)

        /**
         * Создаёт тип.
         */
        fun ofKlass(name: String) =
            ofKlass(klassOf(name))

        /**
         * Создаёт тип.
         */
        fun ofKlass(klass: Klass): VirtualType =
            TYPES[klass.name] ?: createOfKlass(klass)

        /**
         * Создаёт тип.
         */
        private fun createOfKlass(klass: Klass): VirtualType =
            Impl(klass.name).apply {
                TYPES[klass.name] = this
                klass.superclass?.let { parents.add(ofKlass(it.name)) }
                Arrays.stream(klass.interfaces).map { ofKlass(it.name) }.forEach(parents::add)
                componentType = klass.componentType?.let(Companion::ofKlass)
                isInterface = klass.isInterface
                isAbstract = Modifier.isAbstract(klass.modifiers)
                isFinal = Modifier.isFinal(klass.modifiers) || klass.isEnum
                fields += klass.declaredFields.map(VirtualField.Companion::of)
                methods += klass.declaredConstructors.map(VirtualMethod.Companion::of)
                methods += klass.declaredMethods.map(VirtualMethod.Companion::of)
            }
    }

    /**
     * Простая реализация виртуального типа.
     */
    class Impl(
        override var name: String,
        //
        override var parents: MutableList<VirtualType> = ArrayList(),
        override var fields: MutableList<VirtualField> = ArrayList(),
        override var methods: MutableList<VirtualMethod> = ArrayList(),
        //
        override var componentType: VirtualType? = null,
        //
        override var isInterface: Boolean = false,
        override var isAbstract: Boolean = false,
        override var isFinal: Boolean = false,
        override var isFile: Boolean = false,
    ) : VirtualType()
}