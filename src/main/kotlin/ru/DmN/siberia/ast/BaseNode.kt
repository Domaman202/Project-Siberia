package ru.DmN.siberia.ast

import ru.DmN.siberia.utils.meta.IMetadataKey
import ru.DmN.siberia.utils.meta.MetadataContainer
import ru.DmN.siberia.utils.node.INodeInfo

/**
 * Базовая AST нода
 */
open class BaseNode(override val info: INodeInfo) : Node() {
    open val metadata: Lazy<MetadataContainer> = lazy { MetadataContainer() }

    override fun setMetadata(key: IMetadataKey, value: Any?) {
        metadata.value[key] = value
    }

    override fun visitMetadata(key: IMetadataKey, value: Any?) {
        if (metadata.isInitialized()) {
            metadata.value.visit(key, value)
        }
    }

    override fun getMetadata(key: IMetadataKey): Any? =
        if (metadata.isInitialized())
            metadata.value[key]
        else null
}