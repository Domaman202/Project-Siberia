package ru.DmN.siberia.lexer

import ru.DmN.siberia.lexer.Token.Type.*
import ru.DmN.siberia.utils.isPrimitive

/**
 * Лексический анализатор
 */
class Lexer(private val input: String) : Iterator<Token?> {
    private var ptr: Int = 0
    private var line: Int = 0
    private var symbols: Int = 0

    /**
     * Проверка на наличие следующего токена.
     */
    override fun hasNext(): Boolean = ptr != input.length

    /**
     * Выдаёт следующий токен если таковой есть, в противном случае null.
     */
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
            '^', '#' -> {
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
                return str.toString().let {
                    Token(
                        line,
                        if (char == '^') if (it.isPrimitive()) PRIMITIVE else CLASS else NAMING,
                        it
                    )
                }
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
                    var sc = symbols
                    println("symbols $symbols")
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
                                    sc = symbols
                                    c
                                }
                                ' ' -> {
                                    println("sc $sc")
                                    if (sc-- < 0) c
                                    else continue
                                }
                                '\t' -> {
                                    println("sc $sc")
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

                val str = StringBuilder()
                str.append(char)
                while (ptr < input.length) {
                    val c = input[ptr]
                    if (c.isWhitespace() || c == '(' || c == ')' || c == '[' || c == ']')
                        break
                    else {
                        inc()
                        str.append(c)
                    }
                }
                return str.toString().let {
                    Token(line, if (it == "nil") NIL else if (it == "true" || it == "false") BOOLEAN else if (it.endsWith('^')) CLASS else OPERATION, it)
                }
            }
        }
    }

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