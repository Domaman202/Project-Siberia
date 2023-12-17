package ru.DmN.siberia.node

enum class NodeTypes(override val tokenText: String) : INodeType {
    // e
    EXPORT("export"),
    // p
    PROGN("progn"),
    // u
    USE("use"),
    USE_("use"),
    USE_CTX("use-ctx"),
    USE_CTX_("use-ctx")
}