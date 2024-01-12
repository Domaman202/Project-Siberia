package ru.DmN.siberia.console

import ru.DmN.siberia.console.utils.Argument
import ru.DmN.siberia.console.utils.ArgumentType
import ru.DmN.siberia.console.utils.Command

object BaseCommands {
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
    fun alviseAvailable(console: Console): Boolean =
        true

    private fun appendArguments(sb: StringBuilder, command: Command) {
        for (argument in command.arguments) {
            sb.append(" <").append(argument.option).append('>')
        }
    }
}