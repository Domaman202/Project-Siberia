package ru.DmN.siberia.console.commands

import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.module.utils.getOrLoadModule
import ru.DmN.siberia.console.Console
import ru.DmN.siberia.console.ctx.module
import ru.DmN.siberia.console.utils.Argument
import ru.DmN.siberia.console.utils.ArgumentType
import ru.DmN.siberia.console.utils.Command
import ru.DmN.siberia.processor.utils.isPlatform
import ru.DmN.siberia.processor.utils.mp
import ru.DmN.siberia.processor.utils.platform
import java.io.File

object ModuleSelect : Command(
    "module",
    "m",
    "Модуль",
    "Выбрать модуль",
    "Выбирает модуль для дальнейшей работы.",
    listOf(
        Argument(
            "name",
            "Имя",
            ArgumentType.STRING,
            "Имя модуля.",
            "Введите имя модуля"
        )
    )
) {
    override fun available(console: Console): Boolean =
        console.isPlatform

    override fun builderAvailable(flags: Map<Any?, Any?>): Boolean =
        flags["platform"] != null

    override fun action(console: Console, vararg args: Any?) {
        val name = args[0] as String
        //
        val mp = console.mp
        val platform = console.platform
        //
        if (validateModule(console, name))
            return
        val module = mp.getOrLoadModule(name, platform)
        console.module = module
        module.init(platform, mp)
        console.println("Выбран модуль '${module.name}'.")
    }

    override fun builderAppend(arguments: List<Any?>, flags: MutableMap<Any?, Any?>) {
        flags["module"] = arguments[0]
    }

    private fun validateModule(console: Console, name: String): Boolean {
        if (Module::class.java.getResourceAsStream("/$name/module.pht") == null) {
            val file = File("$name/module.pht")
            if (!file.exists() || !file.isFile) {
                console.println("Не удалось найти модуль '${name}'.")
                return true
            }
        }
        return false
    }
}