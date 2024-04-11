package ru.DmN.siberia.console.commands

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.console.Console
import ru.DmN.siberia.console.utils.Argument
import ru.DmN.siberia.console.utils.ArgumentType
import ru.DmN.siberia.console.utils.Command
import ru.DmN.siberia.processor.utils.mp
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.processor.utils.tp
import ru.DmN.siberia.utils.IPlatform
import ru.DmN.siberia.utils.vtype.TypesProvider

object PlatformSelect : Command(
    "platform",
    "p",
    "Модуль",
    "Выбрать платформу",
    "Выбирает целевую платформу для дальнейшей работы.",
    listOf(
        Argument(
            "name",
            "Название",
            ArgumentType.STRING,
            "Название платформы.",
            "Введите название платформы"
        )
    )
) {
    override fun available(console: Console): Boolean =
        true

    override fun builderAvailable(flags: Map<Any?, Any?>): Boolean =
        true

    override fun action(console: Console, vararg args: Any?) {
        val name = (args[0] as String).uppercase()
        //
        val platform = IPlatform.PLATFORMS.find { it.name == name }
        if (platform == null) {
            console.println("Платформа '$name' не найдена!")
            console.stop(1)
            return
        }
        //
        console.platform = platform
        console.tp = TypesProvider.of(platform)
        console.mp = ModulesProvider.of(platform)
        //
        console.println("Выбрана платформа '$name'.")
    }

    override fun builderAppend(arguments: List<Any?>, flags: MutableMap<Any?, Any?>) {
        flags["platform"] = (arguments[0] as String).lowercase()
    }
}