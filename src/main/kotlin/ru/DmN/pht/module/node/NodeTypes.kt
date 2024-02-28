package ru.DmN.pht.module.node

import ru.DmN.siberia.utils.node.INodeType

enum class NodeTypes(override val operation: String) : INodeType {
    VALUE_LIST("value-list"),
    ARGUMENT("argument"),
    MODULE("module"),
    VALUE("value");

    override val processable: Boolean
        get() = false
    override val compilable: Boolean
        get() = false
}