package ru.DmN.siberia.lexer

import ru.DmN.siberia.lexer.Token.DefaultType.*
import ru.DmN.siberia.utils.isPrimitive

/**
 * Стандартная реализация лексического анализатора.
 */
class LexerImpl(val input: String) : Lexer() {
    var ptr: Int = 0
    var line: Int = 0
    var symbols: Int = 0

    override fun hasNext(): Boolean = ptr != input.length

    override fun next(): Token? {
        if (ptr >= input.length)
            return null
        while (ptr < input.length) {
            when (input[ptr]) {
                ' ', '\t', '\n' -> inc()
                else -> break
            }
        }
        if (ptr >= input.length)
            return null
        when (val char = input[inc()]) {
            '(' -> return Token(line, OPEN_BRACKET, "(")
            ')' -> return Token(line, CLOSE_BRACKET, ")")
            '[' -> return Token(line, OPEN_CBRACKET, "[")
            ']' -> return Token(line, CLOSE_CBRACKET, "]")
            '^' -> {
                val sb = StringBuilder()
                while (ptr < input.length) {
                    var c = input[ptr]
                    if (c.isLetter() || c.isDigit() || c == '.' || c == '/' || c == '$') {
                        inc()
                        sb.append(c)
                    } else if (c == '<') {
                        while (c != '>') {
                            c = input[inc()]
                            sb.append(c)
                        }
                        return Token(line, CLASS_WITH_GEN, sb.toString())
                    } else break
                }
                return Token(line, if (sb.isPrimitive()) PRIMITIVE else CLASS, sb.toString())
            }

            '#' -> {
                val str = StringBuilder()
                while (ptr < input.length) {
                    val c = input[ptr]
                    if (c.isWhitespace() || c == '(' || c == ')' || c == '[' || c == ']')
                        break
                    else {
                        inc()
                        str.append(c)
                    }
                }
                return Token(line, NAMING, str.toString())
            }
            '\'' -> {
                inc()
                return Token(line, CHAR, input[inc() - 1].toString())
            }
            '"' -> {
                if (input[ptr] == '"' && input[ptr + 1] == '"') {
                    // MULTILINE
                    inc()
                    inc()
                    val str = StringBuilder()
                    var prev: Char? = null
                    var flag = true
                    var sc = symbols
                    while (true) {
                        val c = input[ptr++]
                        str.append(
                            when (c) {
                                '"' -> {
                                    if (prev != '\\' && input[ptr] == '"' && input[ptr + 1] == '"') {
                                        ptr += 2
                                        break
                                    } else '\"'
                                }
                                'n' -> if (prev == '\\') '\n' else 'n'
                                't' -> if (prev == '\\') '\t' else 't'
                                '\\' -> {
                                    prev = if (prev == '\\') {
                                        str.append(c)
                                        null
                                    } else c
                                    continue
                                }
                                '\n' -> {
                                    if (flag) {
                                        flag = false
                                        continue
                                    }
                                    sc = symbols
                                    c
                                }
                                ' ' -> {
                                    if (sc-- < 0) c
                                    else continue
                                }
                                '\t' -> {
                                    sc -= 4
                                    if (sc + 4 < 0) c
                                    else continue
                                }

                                else -> c
                            }
                        )
                    }
                    return Token(line, STRING, str.toString())
                } else {
                    // NORMAL STRING
                    val str = StringBuilder()
                    var prev: Char? = null
                    while (true) {
                        val c = input[inc()]
                        str.append(
                            when (c) {
                                '"' -> if (prev == '\\') '\"' else break
                                'n' -> if (prev == '\\') '\n' else 'n'
                                't' -> if (prev == '\\') '\t' else 't'
                                '\\' -> {
                                    prev = if (prev == '\\') {
                                        str.append(c)
                                        null
                                    } else c
                                    continue
                                }

                                else -> c
                            }
                        )
                        prev = c
                    }
                    return Token(line, STRING, str.toString())
                }
            }

            else -> {
                if (char.isDigit() || (char == '-' && input[ptr].isDigit())) {
                    val str = StringBuilder()
                    str.append(char)
                    while (ptr < input.length) {
                        val c = input[ptr]
                        if (c.isDigit() || c == '.') {
                            inc()
                            str.append(c)
                        } else break
                    }
                    val type = when (input[inc()]) {
                        'i' -> INTEGER
                        'l' -> LONG
                        'f' -> FLOAT
                        'd' -> DOUBLE
                        else -> {
                            ptr--
                            if (str.contains('.'))
                                DOUBLE
                            else INTEGER
                        }
                    }
                    return Token(line, type, str.toString())
                }

                val sb = StringBuilder()
                sb.append(char)
                while (ptr < input.length) {
                    val c = input[ptr]
                    if (c.isWhitespace() || c == '(' || c == ')' || c == '[' || c == ']')
                        break
                    else {
                        inc()
                        sb.append(c)
                    }
                }
                val str = sb.toString()
                return Token(
                    line,
                    when (str) {
                        "nil" -> NIL
                        "false", "true" -> BOOLEAN
                        else -> OPERATION
                    },
                    str
                )
            }
        }
    }

    /**
     * Инкриментирует указатель.
     * Прибавляет нужные переменные (line / symbols) в случае необходимости.
     */
    private fun inc(): Int {
        val i = ptr++
        return when (input[i]) {
            '\n' -> {
                line++
                symbols = 0
                ptr
            }
            '\t' -> {
                symbols += 4
                i
            }
            else -> {
                symbols++
                i
            }
        }
    }
}