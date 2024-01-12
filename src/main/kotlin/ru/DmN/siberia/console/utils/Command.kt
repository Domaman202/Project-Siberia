package ru.DmN.siberia.console.utils

/**
 * Команда
 */
data class Command(
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
    val arguments: List<Argument>,

    /**
     * Проверяет доступность команды.
     */
    val available: CommandCheck,

    /**
     * Действие, которое будет выполняться при запуске команды.
     */
    val action: CommandAction
)