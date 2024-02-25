package ru.DmN.siberia.utils

/**
 * Целевая платформа.
 */
interface IPlatform {
    /**
     * Имя платформы (Большими буквами).
     */
    val name: String

    companion object {
        val PLATFORMS: MutableList<IPlatform> = ArrayList()
    }

    /**
     * Универсальная платформа.
     */
    object UNIVERSAL : IPlatform {
        override val name: String
            get() = "UNIVERSAL"
    }
}