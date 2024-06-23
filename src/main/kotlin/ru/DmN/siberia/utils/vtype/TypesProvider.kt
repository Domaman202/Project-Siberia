package ru.DmN.siberia.utils.vtype

import ru.DmN.siberia.utils.IPlatform
import java.util.function.Supplier

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
        typeOfOrNull(name) ?: throw ClassNotFoundException("Type '$name' not founded!")

    /**
     * Ищет тип по имени, иначе возвращает null.
     */
    open fun typeOfOrNull(name: String): VirtualType? =
        types[name.hashCode()]

    /**
     * Добавляет тип в список типов.
     */
    open operator fun plusAssign(type: VirtualType) {
        types[type.name.hashCode()] = type
    }

    companion object {
        private val PROVIDERS: MutableMap<IPlatform, Supplier<TypesProvider>> = HashMap()

        fun add(platform: IPlatform, provider: Supplier<TypesProvider>) {
            PROVIDERS[platform] = provider
        }

        fun of(platform: IPlatform) =
            PROVIDERS[platform]?.get() ?: throw RuntimeException("Поставщик типов для платформы '$platform' не найден!")
    }

    class VoidTypesProvider : TypesProvider()
}