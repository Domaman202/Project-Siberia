package ru.DmN.siberia.utils.vtype

/**
 * Имя без пакета.
 */
val VirtualType.simpleName: String
    get() = this.name.substring(this.name.lastIndexOf('.') + 1)