package ru.DmN.siberia.console.commands

import ru.DmN.siberia.console.Console
import ru.DmN.siberia.console.utils.ArgumentType
import ru.DmN.siberia.console.utils.Command

object CmdBuilder : Command(
    null,
    null,
    "Помощь",
    "Сборщик опций консоли",
    "Собирает быструю команду для запуска нужных действий в консоли.",
    emptyList()
) {
    override fun available(console: Console): Boolean =
        console.interactive

    override fun builderAvailable(flags: Map<Any?, Any?>): Boolean =
        false

    override fun action(console: Console, vararg args: Any?) {
        val out = StringBuilder()
        val flags = HashMap<Any?, Any?>()
        while (true) {
            console.clear()
            console.println("Команды:")
            var category: String? = null
            val availableCommands = console.commands.filter { it.builderAvailable(flags) }
            availableCommands.forEachIndexed { i, it ->
                if (category != it.category) {
                    category = it.category
                    console.println("\n[T] (${it.category})")
                }
                console.print(if (availableCommands.size == i + 1 || availableCommands.getOrNull(i + 1)?.category != category) "[V] " else "[|] ")
                console.println("$i. ${it.name}")
            }
            val index = console.readString("\n[ $out]\nВведите команду (или . для завершения сборки)")
            if (index == ".")
                break
            val i = index.toInt()
            if (availableCommands.size > i) {
                console.clear()
                val cmd = availableCommands[i]
                out.append('-').append(cmd.shortOption).append(' ')
                val arguments = cmd.arguments.map {
                    when (it.type) {
                        ArgumentType.STRING -> console.readString(it.consoleText)
                        ArgumentType.INT -> console.readInt(it.consoleText)
                        else -> throw UnsupportedOperationException("Аргумент типа '${it.type}' не поддерживается!")
                    }
                }
                arguments.forEach { out.append(it).append(' ') }
                cmd.builderAppend(arguments, flags)
            } else console.println("Команда №$index не найдена!\n")
        }
        console.println(out)
    }
}