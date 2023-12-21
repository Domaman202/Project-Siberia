package ru.DmN.siberia.node

enum class NodeTypes : INodeType {
    // e
    EXPORT("export", Type.PARSED),
    // p
    PROGN("progn", Type.PARSED),
    PROGN_("progn", Type.PROCESSED),
    // u
    USE("use", Type.PARSED),
    USE_("use", Type.PROCESSED),
    USE_CTX("use-ctx", Type.PARSED),
    USE_CTX_("use-ctx", Type.PROCESSED);


    override val operation: String
    override val processable: Boolean
    override val compilable: Boolean

    constructor(operation: String, processable: Boolean, compilable: Boolean) {
        this.operation = operation
        this.processable = processable
        this.compilable = compilable
    }

    constructor(operation: String, type: Type) {
        this.operation = operation
        if (type == Type.PARSED) {
            this.processable = true
            this.compilable = false
        } else {
            this.processable = false
            this.compilable = true
        }
    }

    enum class Type {
        PARSED,
        PROCESSED
    }
}