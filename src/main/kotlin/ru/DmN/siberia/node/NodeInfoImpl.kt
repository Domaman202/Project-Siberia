package ru.DmN.siberia.node

/**
 * Стандартная реализация INodeInfo.
 */
class NodeInfoImpl(override val type: INodeType, override val file: String?, override val line: Int?) : INodeInfo {
    override fun withType(type: INodeType): INodeInfo =
        NodeInfoImpl(type, file, line)

    override fun print(message: String): String {
        val sb = StringBuilder().append("[\n| message:")
        if (message.contains('\n')) {
            val i = message.lastIndexOf('\n')
            sb.append("\n|- ").append(message.substring(0, i)).append(message.substring(i).replace("\n", "\n|- ")).append('\n')
        } else sb.append(' ').append(message).append('\n')
        sb.append("| file: ").append(file).append("\n| line: ").append(line).append("\n]")
        return sb.toString()
    }

    override fun print(): String =
        """
            [
            | type: $type
            | file: $file
            | line: $line
            ]
        """.trimIndent()

    override fun equals(other: Any?): Boolean =
        this === other || (other is NodeInfoImpl && other.type == type && other.file == file && other.line == line)

    override fun hashCode(): Int =
        (type.operation.hashCode() * 31 + (file?.hashCode() ?: 0))  * 31 + (line?.hashCode() ?: 0)
}