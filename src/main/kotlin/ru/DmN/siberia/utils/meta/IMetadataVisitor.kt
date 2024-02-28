package ru.DmN.siberia.utils.meta

fun interface IMetadataVisitor {
    fun onUpdate(key: IMetadataKey, value: Any?)
}