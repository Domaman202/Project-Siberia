package ru.DmN.siberia.utils

import ru.DmN.siberia.utils.vtype.VirtualType

class VariableWithGenerics(
    name: String,
    type: VirtualType?,
    id: Int,
    tmp: Boolean,
    /**
     * Generic's
     */
    val generics: List<VirtualType>
) : Variable(name, type, id, tmp) {
    override fun equals(other: Any?): Boolean =
        super.equals(other) && other is VariableWithGenerics && generics == other.generics

    override fun hashCode(): Int =
        super.hashCode() * 31 + generics.hashCode()
}