package ru.DmN.siberia.utils.meta

class MetadataContainer(
    private val metadata: MutableMap<IMetadataKey, Any?> = HashMap(),
    private val visitors: MutableList<IMetadataVisitor> = ArrayList()
) {
    fun copy() =
        MetadataContainer(metadata)

    operator fun set(key: IMetadataKey, value: Any?) {
        visit(key, value)
        if (value == null)
            metadata.remove(key)
        else metadata[key] = value
    }

    fun visit(key: IMetadataKey, value: Any?) {
        visitors.forEach { it.onUpdate(key, value) }
    }

    operator fun get(key: IMetadataKey): Any? =
        metadata[key]

    fun addVisitor(visitor: IMetadataVisitor) {
        visitors += visitor
    }

    fun removeVisitor(visitor: IMetadataVisitor) {
        visitors -= visitor
    }
}
