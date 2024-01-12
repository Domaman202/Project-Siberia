package ru.DmN.siberia.console.utils

data class Argument(
    /**
     * Аргумент в виде опции.
     */
    val option: String?,

    /**
     * Имя аргумента.
     */
    val name: String,

    /**
     * Тип аргумента
     */
    val type: ArgumentType,

    /**
     * Описание аргумента.
     */
    val description: String,

    /**
     * Текст, который будет написан при получении аргумента при консольном выполнении команды.
     */
    val consoleText: String
)