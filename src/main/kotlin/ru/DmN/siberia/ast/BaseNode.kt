package ru.DmN.siberia.ast

import ru.DmN.siberia.utils.node.INodeInfo
import ru.DmN.siberia.utils.meta.IMetadataKey
import ru.DmN.siberia.utils.meta.MetadataContainer

/**
 * Базовая AST нода
 */
open class BaseNode(override val info: INodeInfo) : Node() {
    open val metadata: Lazy<MetadataContainer> = lazy { MetadataContainer() }

    override fun setMetadata(key: IMetadataKey, value: Any?) {
        metadata.value[key] = value
    }

    override fun getMetadata(key: IMetadataKey): Any? =
        metadata.value[key]
}