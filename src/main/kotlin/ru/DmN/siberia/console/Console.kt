package ru.DmN.siberia.console

import ru.DmN.siberia.console.utils.ArgumentType
import ru.DmN.siberia.console.utils.Command
import ru.DmN.siberia.utils.ctx.IContextCollection
import ru.DmN.siberia.utils.ctx.IContextKey
import java.io.PrintStream
import kotlin.io.readln as kreadln

/**
 * Консоль.
 *
 * Реализуйте класс для создания своей консоли.
 */
open class Console : Runnable, IContextCollection<Console> {
    /**
     * Консоль взаимодействует с пользователем или выполняет команды автономно?
     */
    var interactive: Boolean = false
        protected set

    /**
     * Список команд.
     */
    val commands: MutableList<Command> = ArrayList()

    /**
     * Контексты.
     */
    override val contexts: MutableMap<IContextKey, Any?> = HashMap()

    /**
     * Остановка консоли в неинтерактивном режиме.
     *
     * @param code Код выхода.
     */
    open fun stop(code: Int) {
        if (!interactive) {
            Runtime.getRuntime().exit(code)
            throw Error("Выход")
        }
    }

    /**
     * Очистка консоли.
     */
    open fun clear() {
        print.print("\n\n\n\n\n\n\n\n\n\n\n\n")
//        Runtime.getRuntime().exec("clear")
    }

    /**
     * Вывод сообщения с переносом строки.
     *
     * @param msg Сообщение.
     */
    open fun println(msg: Any?): Unit =
        print.println(msg)

    /**
     * Вывод сообщения без переноса строки.
     *
     * @param msg Сообщение.
     */
    open fun print(msg: Any?): Unit =
        print.print(msg)

    /**
     * Поток вывода.
     */
    open val print: PrintStream
        get() = System.out

    /**
     * Ввод комманды.
     *
     * @param text Текст который будет напечатан перед вводом.
     * @param available Показать только доступные команды.
     */
    open fun selectCommand(text: String, available: Boolean): Command {
        println("Команды:")
        var category: String? = null
        val availableCommands = commands.filter { !available || it.available(this) }
        availableCommands.forEachIndexed { i, it ->
            if (category != it.category) {
                category = it.category
                println("\n[T] (${it.category})")
            }
            print(if (availableCommands.size == i + 1 || availableCommands.getOrNull(i + 1)?.category != category) "[V] " else "[|] ")
            println("$i. ${it.name}")
        }
        while (true) {
            val index = readInt("\n$text")
            if (availableCommands.size > index) {
                print('\n')
                return availableCommands[index]
            }
            println("Команда №$index не найдена!\n")
        }
    }

    /**
     * Ввод строки.
     *
     * @param text Текст который будет напечатан перед вводом.
     */
    open fun readString(text: String): String {
        print("$text:\n> ")
        return kreadln()
    }

    /**
     * Ввод числа.
     *
     * @param text Текст который будет напечатан перед вводом.
     */
    open fun readInt(text: String): Int {
        while (true) {
            readString(text).let {
                if (it.isNotEmpty() && Regex("-?\\d+").matches(it)) {
                    return it.toInt()
                }
            }
        }
    }

    override fun run() {
        commands.sortBy { it.category }
        while (true) {
            val cmd = selectCommand("Выберите команду", true)
            clear()
            val args = arrayOfNulls<Any?>(cmd.arguments.size)
            for (i in args.indices) {
                val argument = cmd.arguments[i]
                args[i] = when (argument.type) {
                    ArgumentType.COMMAND -> selectCommand(argument.consoleText, false)
                    ArgumentType.STRING -> readString(argument.consoleText)
                    ArgumentType.INT -> readInt(argument.consoleText)
                }
            }
            cmd.action(this, *args)
            kreadln()
            clear()
        }
    }

    override fun with(key: IContextKey, ctx: Any?): Console {
        contexts[key] = ctx
        return this
    }
}