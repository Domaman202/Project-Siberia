package ru.DmN.siberia.utils.node

interface ITokenInfo {
    val file: String
    val line: Int
    val symbol: Int
    val length: Int

    fun copy() = this
}