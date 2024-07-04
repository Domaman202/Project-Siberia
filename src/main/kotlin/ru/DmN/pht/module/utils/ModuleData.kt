package ru.DmN.pht.module.utils

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.utils.IPlatform

/**
 * Информация о модуле, храниться в пределах поставщика модуля.
 */
data class ModuleData(
    /**
     * Модуль.
     */
    val module: Module,

    /**
     * Обработанные ноды.
     */
    val processedNodes: MutableMap<IPlatform, MutableList<Node>> = HashMap()
) {
    companion object {
        fun of(module: Module): ModuleData =
            ModuleData(module)
    }
}