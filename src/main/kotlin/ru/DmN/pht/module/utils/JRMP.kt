package ru.DmN.pht.module.utils

import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.Siberia

/**
 * Java Runtime Modules Provider
 */
class JRMP : ModulesProvider() {
    init {
        this += Siberia
        this += StdModule
    }
}