package ru.DmN.siberia

/**
 * Консоль.
 *
 * Реализуйте класс для создания своей консоли.
 */
abstract class Console : Runnable {
    val actions: MutableList<Triple<String, String, Runnable>> = ArrayList()

    override fun run() {
        while (true) {
            println("\nДействия:")
            actions.forEachIndexed { i, it -> println("$i. ${it.first}") }
            print("\nВыберите действие:\n> ")
            val index = readln().toInt()
            val action = actions.getOrNull(index)
            if (action == null)
                println("Действие №$index не найдено!\n")
            else {
                println()
                action.third.run()
            }
        }
    }
}