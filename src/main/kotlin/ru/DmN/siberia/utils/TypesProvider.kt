package ru.DmN.siberia.utils

import ru.DmN.siberia.processor.utils.Platforms
import ru.DmN.siberia.processor.utils.Platforms.JVM
import ru.DmN.siberia.processor.utils.Platforms.UNIVERSAL

/**
 * Провайдер типов.
 */
abstract class TypesProvider {
    /**
     * Список типов.
     */
    val types: MutableMap<Int, VirtualType> = HashMap()

    /**
     * Ищет тип по имени, иначе выкидывает исключение.
     */
    open fun typeOf(name: String): VirtualType =
        types[name.hashCode()] ?: throw RuntimeException("Type '$name not founded!'")

    /**
     * Ищет тип по имени, иначе возвращает null.
     */
    open fun typeOfOrNull(name: String): VirtualType? =
        try { typeOf(name) } catch (_: ClassNotFoundException) { null }

    /**
     * Добавляет тип в список типов.
     */
    open operator fun plusAssign(type: VirtualType) {
        types[type.name.hashCode()] = type
    }

    companion object {
        fun void() =
            VoidTypesProvider()

        fun java() =
            JRTP()

        fun of(platform: Platforms) =
            when (platform) {
                JVM -> java()
                UNIVERSAL -> void()
            }
    }

    class VoidTypesProvider : TypesProvider()
}