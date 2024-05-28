package ru.DmN.siberia.utils.node

/**
 * Стандартная реализация ITokenInfo
 */
class TokenInfoImpl(
    override val file: String,
    override val line: Int,
    override val symbol: Int,
    override val length: Int
) : ITokenInfo {
    override fun hashCode(): Int =
        file.hashCode() + line * 31 + symbol * 31 * 2 + length * 31 * 3
}