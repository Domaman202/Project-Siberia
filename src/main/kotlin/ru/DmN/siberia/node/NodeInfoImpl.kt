package ru.DmN.siberia.node

import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parser.utils.file

/**
 * Стандартная реализация INodeInfo.
 */
class NodeInfoImpl(override val type: INodeType, override val file: String?, override val line: Int?) : INodeInfo {
    override fun withType(type: INodeType): INodeInfo =
        NodeInfoImpl(type, file, line)

    companion object {
        fun of(type: INodeType, ctx: ParsingContext, token: Token) =
            NodeInfoImpl(type, ctx.file, token.line)

        fun of(type: INodeType) =
            NodeInfoImpl(type, null, null)
    }
}