package ru.DmN.siberia.utils.exception

/**
 * Специальное исключение для обхода вышестоящего блока try-catch.
 */
class NoCatherWrappedException(override val cause: Throwable, var i: Int = 1) : Exception(cause)