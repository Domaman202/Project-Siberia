package ru.DmN.pht.module.utils

import ru.DmN.siberia.utils.IPlatform

abstract class ModulesProvider {
    /**
     * Список модулей.
     */
    val modules: MutableMap<Int, Module> = HashMap()

    /**
     * Получает модуль, в случае его отсутствия либо выполняет метод для его добавления.
     *
     * @param name Имя модуля.
     * @param put Метод для добавления модуля.
     * @return Модуль.
     */
    fun getOrPut(name: String, put: () -> Module): Module =
        this[name] ?: put().apply { this@ModulesProvider += this@apply }

    /**
     * Ищет модуль по имени, иначе выкидывает исключение.
     */
    open fun getOrThrow(name: String): Module =
        modules[name.hashCode()] ?: throw RuntimeException("Module '$name not founded!'")

    /**
     * Ищет модуль по имени, иначе возвращает null.
     */
    open operator fun get(name: String): Module? =
        modules[name.hashCode()]

    /**
     * Добавляет модуль в список модулей.
     */
    open operator fun plusAssign(module: Module) {
        modules[module.name.hashCode()] = module
    }

    companion object {
        fun of(platforms: IPlatform) =
            JRMP()
    }
}