package ru.DmN.siberia.console

import ru.DmN.siberia.console.utils.ArgumentType

open class BaseConsole : Console() {
    fun run(args: Array<String>) {
        if (args.isEmpty()) {
            interactive = true
            run()
        } else {
            interactive = false
            var i = 0
            while (i < args.size) {
                val input = args[i++]
                val command =
                    if (input.startsWith("--")) {
                        val option = input.substring(2)
                        this.commands.find { it.option == option }
                    } else if (input.startsWith("-")) {
                        val shortOption = input.substring(1)
                        this.commands.find { it.shortOption == shortOption }
                    } else throw RuntimeException("Ожидалась команда, получено '$input'.")
                command ?: throw RuntimeException("Команда '$input' не найдена.")
                val arguments = arrayOfNulls<Any?>(command.arguments.size)
                command.arguments.forEachIndexed { j, it ->
                    if (args[i].startsWith("-"))
                        throw RuntimeException("Ожидался аргумент '${it.name}', получена команда '${args[i]}.'")
                    arguments[j] = when (it.type) {
                        ArgumentType.AVAILABLE_COMMAND -> this.commands.filter { it.available(this) }.find { it.name == args[i++] }!!
                        ArgumentType.COMMAND -> this.commands.find { it.name == args[i++] }!!
                        ArgumentType.STRING -> args[i++]
                        ArgumentType.INT -> args[i++].toInt()
                    }
                }
                command.action(this, *arguments)
            }
        }
    }
}