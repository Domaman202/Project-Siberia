package ru.DmN.siberia.utils

import ru.DmN.siberia.Parser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.lexer.*
import java.io.DataInputStream
import java.io.InputStream

inline fun <T, R> List<T>.mapMutable(transform: (T) -> R): MutableList<R> {
    val list = ArrayList<R>(this.size)
    for (it in this)
        list.add(transform(it))
    return list
}

fun Map<String, Any?>.copy(): MutableMap<String, Any?> {
    val map = HashMap<String, Any?>()
    this.forEach { map[it.key] = it.value }
    return map
}

val Node.operation
    get() = info.type.operation

/**
 * Читает все байты из потока в массив.
 */
fun InputStream.readBytes(): ByteArray {
    val bytes = ByteArray(available())
    DataInputStream(this).readFully(bytes)
    return bytes
}

fun <T> Map<Regex, T>.getRegex(key: String): T? =
    entries.find { key.matches(it.key) }?.value

/**
 * Возвращает дексриптор типа.
 */
val String.desc
    get() = when (this) {
        "void" -> "V"
        "boolean" -> "Z"
        "byte" -> "B"
        "short" -> "S"
        "char" -> "C"
        "int" -> "I"
        "long" -> "J"
        "double" -> "D"
        else -> {
            if (this[0] == '[') {
                var i = 0
                while (this[i] == '[') i++
                val clazz = this.substring(i)
                if (this[1] == 'L' || clazz.isPrimitive())
                    this.className
                else "${this.substring(0, i)}L${clazz.className};"
            }
            else "L${this.className};"
        }
    }

/**
 * Переводит тип в имя класса.
 */
val String.className
    get() = this.replace('.', '/')

fun Parser.nextOpenCBracket(): Token = this.nextToken()!!.checkOpenCBracket()
fun Parser.nextCloseCBracket(): Token = this.nextToken()!!.checkCloseCBracket()
fun Parser.nextOperation(): Token = this.nextToken()!!.checkOperation()
fun Parser.nextType(): Token = this.nextToken()!!.checkType()
fun Parser.nextNaming(): Token = this.nextToken()!!.checkNaming()

typealias Klass = Class<*>

/**
 * Получает класс по имени / дескриптору
 */
fun klassOf(name: String): Klass =
    if (name.isPrimitive())
        name.getPrimitive()
    else Class.forName(name.let { if (name.startsWith('L') && name.endsWith(';')) name.substring(1, name.length - 1).replace('/', '.') else name }) as Klass

/**
 * Возвращает дескриптор класса
 */
val Klass.desc
    get() =
        if (name.isPrimitive())
            when (name) {
                "void" -> "V"
                "boolean" -> "Z"
                "byte" -> "B"
                "char" -> "C"
                "short" -> "S"
                "int" -> "I"
                "long" -> "J"
                "float" -> "F"
                "double" -> "D"
                else -> throw RuntimeException()
            }
        else "L${name.replace('.', '/')};"


/**
 * Возвращает примитивный класс от типа.
 */
fun String.getPrimitive(): Klass {
    return when (this) {
        "void" -> Void::class.javaPrimitiveType
        "boolean" -> Boolean::class.javaPrimitiveType
        "byte" -> Byte::class.javaPrimitiveType
        "char" -> Char::class.javaPrimitiveType
        "short" -> Short::class.javaPrimitiveType
        "int" -> Int::class.javaPrimitiveType
        "long" -> Long::class.javaPrimitiveType
        "float" -> Float::class.javaPrimitiveType
        "double" -> Double::class.javaPrimitiveType
        else -> throw RuntimeException()
    } as Klass
}

/**
 * Проверяет на примитивность тип.
 */
fun CharSequence.isPrimitive(): Boolean {
    return when (this) {
        "void",
        "boolean",
        "byte",
        "char",
        "short",
        "int",
        "long",
        "float",
        "double" -> true

        else -> false
    }
}

/**
 * Отступ.
 */
fun StringBuilder.indent(indent: Int): StringBuilder {
    this.append("|\t".repeat(indent))
    return this
}
