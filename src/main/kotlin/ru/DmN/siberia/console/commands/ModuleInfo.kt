package ru.DmN.siberia.console.commands

import ru.DmN.siberia.console.Console
import ru.DmN.siberia.console.ctx.isModule
import ru.DmN.siberia.console.ctx.module
import ru.DmN.siberia.console.utils.Command

object ModuleInfo : Command(
    "module-info",
    "mi",
    "Модуль",
    "Информация о модуле",
    "Выводит информацию о модуле.",
    emptyList()
) {
    override fun available(console: Console): Boolean =
        console.isModule

    override fun builderAvailable(flags: Map<Any?, Any?>): Boolean =
        flags["module"] != null

    override fun action(console: Console, vararg args: Any?) {
        console.module.run {
            console.clear()
            console.println("[T]")
            console.println(" |> Имя: $name")
            console.println(" |> Версия: $version")
            console.println(" |> Автор(ы): $author")
            console.println(" |> Зависимости: $deps")
            console.println(" |> Использует модули: $uses")
            console.println(" |> Исходный код: $sources")
            console.println(" |> Ресурсные: $resources")
            console.println(" |> Инициализация: ${if (init) "инициализирован" else "не инициализирован"}")
            console.println("[V]")
        }
    }
}