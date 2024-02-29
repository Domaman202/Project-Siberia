package ru.DmN.siberia.ast

import ru.DmN.siberia.utils.meta.IMetadataKey
import ru.DmN.siberia.utils.meta.MetadataContainer
import ru.DmN.siberia.utils.node.INodeInfo

/**
 * Базовая AST нода с метаданными.
 */
open class BaseMetaNode(info: INodeInfo, open val metadata: Lazy<MetadataContainer> = lazy { MetadataContainer() }) : BaseNode(info) {
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

    override fun copy(): BaseMetaNode =
        if (metadata.isInitialized())
            BaseMetaNode(info, lazyOf(metadata.value.copy()))
        else BaseMetaNode(info)

    override fun equals(other: Any?): Boolean =
        other is BaseMetaNode && other.metadata == metadata
}