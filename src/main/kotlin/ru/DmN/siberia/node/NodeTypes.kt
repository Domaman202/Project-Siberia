package ru.DmN.siberia.node

enum class NodeTypes(override val operation: String, override val processable: Boolean,
                     override val compilable: Boolean) : INodeType {
    // e
    EXPORT("export", true, false),
    // p
    PROGN("progn", true, false),
    PROGN_("progn", false, true),
    // u
    USE("use", true, false),
    USE_("use", false, true),
    USE_CTX("use-ctx",true, false),
    USE_CTX_("use-ctx", false, true)
}