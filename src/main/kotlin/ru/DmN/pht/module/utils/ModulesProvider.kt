package ru.DmN.pht.module.utils

import ru.DmN.siberia.utils.IPlatform

abstract class ModulesProvider {
    /**
     * Список модулей.
     */
    val modules: MutableMap<Int, ModuleData> = HashMap()

    /**
     * Ищет информацию о модуле по имени, иначе выкидывает исключение.
     *
     * @param name Имя модуля.
     * @return Информация о модуле.
     */
    open fun getModuleData(name: String): ModuleData =
        modules[name.hashCode()] ?: throw RuntimeException("Module '$name not founded!'")

    /**
     * Получает модуль, в случае его отсутствия либо выполняет метод для его добавления.
     *
     * @param name Имя модуля.
     * @param put Метод для добавления модуля.
     * @return Модуль.
     */
    open fun getOrPut(name: String, put: () -> Module): Module =
        this[name] ?: put().apply { this@ModulesProvider += this@apply }

    /**
     * Ищет модуль по имени, иначе выкидывает исключение.
     *
     * @param name Имя модуля.
     * @return Модуль.
     */
    open fun getOrThrow(name: String): Module =
        modules[name.hashCode()]?.module ?: throw RuntimeException("Module '$name not founded!'")

    /**
     * Ищет модуль по имени, иначе возвращает null.
     *
     * @param name Имя модуля.
     * @return Модуль, null - если модуль не найден.
     */
    open operator fun get(name: String): Module? =
        modules[name.hashCode()]?.module

    /**
     * Добавляет модуль в список модулей.
     *
     * @param module Модуль.
     */
    open operator fun plusAssign(module: Module) {
        modules[module.name.hashCode()] = ModuleData.of(module)
    }

    companion object {
        fun of(platforms: IPlatform) =
            JRMP()
    }
}