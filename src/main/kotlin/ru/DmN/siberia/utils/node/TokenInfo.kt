package ru.DmN.siberia.utils.node

class TokenInfo(
    override val file: String,
    override val line: Int,
    override val symbol: Int,
    override val length: Int
) : ITokenInfo