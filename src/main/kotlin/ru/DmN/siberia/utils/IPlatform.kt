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

        operator fun plusAssign(platform: IPlatform) {
            PLATFORMS += platform
        }

        operator fun get(name: String): IPlatform =
            getOrNull(name)!!

        fun getOrNull(name: String): IPlatform? =
            PLATFORMS.find { it.name == name }
    }

    /**
     * Универсальная платформа.
     */
    object UNIVERSAL : IPlatform {
        override val name: String
            get() = "UNIVERSAL"
    }
}