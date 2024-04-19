package ru.DmN.siberia.utils.node

object ErrorNodeType : INodeType {
    override val operation: String
        get() = "#ERROR#"
    override val processable: Boolean
        get() = false
    override val compilable: Boolean
        get() = false
}