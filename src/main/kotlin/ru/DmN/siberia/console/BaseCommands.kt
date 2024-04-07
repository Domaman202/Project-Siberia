@file:Suppress("UNUSED_PARAMETER")
package ru.DmN.siberia.console

import ru.DmN.siberia.console.utils.Argument
import ru.DmN.siberia.console.utils.ArgumentType
import ru.DmN.siberia.console.utils.Command

object BaseCommands {
    @JvmStatic
    val HELP = Command(
        "help",
        "h",
        "Помощь",
        "Помощь",
        "Выводит подробную информацию о команде.",
        listOf(
            Argument(
                "cmd",
                "Команда",
                ArgumentType.COMMAND,
                "Команда.",
                "Выберите команду по которой хотите получить помощь"
            )
        ),
        BaseCommands::alviseAvailable,
        BaseCommands::help
    )

    @JvmStatic
    val CMD_BUILDER = Command(
        null,
        null,
        "Помощь",
        "Сборщик опций консоли",
        "Собирает быструю команду для запуска нужных действий в консоли.",
        emptyList(),
        BaseCommands::interactiveAvailable,
        BaseCommands::cmdBuilder
    )

    val ALL_COMMANDS = listOf(HELP, CMD_BUILDER)

    @JvmStatic
    fun cmdBuilder(console: Console, vararg args: Any?) {
        val out = StringBuilder()
        while (true) {
            console.clear()
            console.println(out)
            console.println("Команды:")
            var category: String? = null
            console.commands.forEachIndexed { i, it ->
                if (category != it.category) {
                    category = it.category
                    console.println("\n[T] (${it.category})")
                }
                console.print(if (console.commands.size == i + 1 || console.commands.getOrNull(i + 1)?.category != category) "[V] " else "[|] ")
                console.println("$i. ${it.name}")
            }
            val index = console.readString("\nВведите команду (или . для завершения сборки)")
            if (index == ".")
                break
            val i = index.toInt()
            if (console.commands.size > i) {
                console.clear()
                val cmd = console.commands[i]
                out.append('-').append(cmd.shortOption).append(' ')
                cmd.arguments.forEach {
                    out.append(
                        when (it.type) {
                            ArgumentType.STRING -> console.readString(it.consoleText)
                            ArgumentType.INT -> console.readInt(it.consoleText)
                            else -> throw UnsupportedOperationException("Аргумент типа '${it.type}' не поддерживается!")
                        }
                    ).append(' ')
                }
            } else console.println("Команда №$index не найдена!\n")
        }
        console.println(out)
    }

    @JvmStatic
    fun help(console: Console, vararg args: Any?) {
        val command = args[0] as Command
        //
        val description =
            if (!command.description.contains('\n'))
                command.description
            else "\n${command.description}".replace("\n", "\n |] ")
        val shortOption = StringBuilder("-").append(command.shortOption)
        appendArguments(shortOption, command)
        val option = StringBuilder("--").append(command.option)
        appendArguments(option, command)
        //
        console.clear()
        console.println("[T]")
        console.println(" |> Имя: ${command.name}")
        console.println(" |> Описание: $description")
        console.println(" |> Короткая опция: $shortOption")
        console.println(" |> Полная опция: $option")
        console.println("[V]")
    }

    @JvmStatic
    fun interactiveAvailable(console: Console) =
        console.interactive

    @JvmStatic
    fun alviseAvailable(console: Console): Boolean =
        true

    private fun appendArguments(sb: StringBuilder, command: Command) {
        for (argument in command.arguments) {
            sb.append(" <").append(argument.option).append('>')
        }
    }
}