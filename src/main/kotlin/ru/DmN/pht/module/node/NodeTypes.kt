package ru.DmN.pht.module.node

import ru.DmN.siberia.node.INodeType

enum class NodeTypes(override val tokenText: String) : INodeType {
    VALUE_LIST("value-list"),
    ARGUMENT("argument"),
    MODULE("module"),
    VALUE("value")
}