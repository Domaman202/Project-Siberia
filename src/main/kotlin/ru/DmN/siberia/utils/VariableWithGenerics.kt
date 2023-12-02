package ru.DmN.siberia.utils

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
    override fun hashCode(): Int =
        super.hashCode() + generics.hashCode()
}