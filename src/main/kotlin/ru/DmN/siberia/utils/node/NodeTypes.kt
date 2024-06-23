package ru.DmN.siberia.utils.node

import ru.DmN.siberia.utils.node.NodeTypes.Type.PARSED
import ru.DmN.siberia.utils.node.NodeTypes.Type.PROCESSED

enum class NodeTypes : INodeType {
    // e
    EXPORT("export", PARSED),
    EXPORT_("export", PROCESSED),
    // o
    EXPORT_ONLY("export-only", false, false),
    // p
    PROGN("progn", PARSED),
    PROGN_("progn", PROCESSED),
    // u
    USE("use", PARSED),
    USE_("use", PROCESSED),
    USE_CTX("use-ctx", PARSED),
    USE_CTX_("use-ctx", PROCESSED);


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
        if (type == PARSED) {
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