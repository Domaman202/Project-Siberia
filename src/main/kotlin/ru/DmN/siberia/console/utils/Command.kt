package ru.DmN.siberia.console.utils

import ru.DmN.siberia.console.Console

/**
 * Команда.
 */
abstract class Command(
    /**
     * Имя команды в виде опции.
     *
     * (--help / --run)
     */
    val option: String?,

    /**
     * Короткое имя команды в виде опции.
     *
     * (-h / -r)
     */
    val shortOption: String?,

    /**
     * Категория команды.
     *
     * (Помощь / Сборка / Отладка)
     */
    val category: String,

    /**
     * Имя команды.
     *
     * (О программе / Сборка & Запуск)
     */
    val name: String,

    /**
     * Описание команды.
     *
     * (Выводит информацию о программе / Собирает и запускает модуль)
     */
    val description: String,

    /**
     * Аргументы команды.
     *
     * (<file> / <command>)
     */
    val arguments: List<Argument>
) {

    /**
     * Проверяет доступность команды.
     */
    abstract fun available(console: Console): Boolean

    /**
     * Проверяет доступность команды для сборщика команд.
     */
    abstract fun builderAvailable(flags: Map<Any?, Any?>): Boolean

    /**
     * Действие, которое будет выполняться при запуске команды.
     */
    abstract fun action(console: Console, vararg args: Any?)

    /**
     * Добавление команды в сборщике команд.
     */
    open fun builderAppend(arguments: List<Any?>, flags: MutableMap<Any?, Any?>): Unit = Unit
}