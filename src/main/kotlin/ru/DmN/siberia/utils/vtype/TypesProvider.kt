package ru.DmN.siberia.utils.vtype

import ru.DmN.siberia.utils.IPlatform

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
        private val PROVIDERS: MutableMap<IPlatform, TypesProvider> = HashMap()

        fun add(platform: IPlatform, provider: TypesProvider) {
            PROVIDERS[platform] = provider
        }

        fun of(platform: IPlatform) =
            PROVIDERS[platform] ?: throw RuntimeException("Поставщик типов для платформы '$platform' не найден!")
    }

    class VoidTypesProvider : TypesProvider()
}