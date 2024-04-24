package ru.DmN.siberia.utils

import ru.DmN.siberia.ast.Node
import java.io.DataInputStream
import java.io.InputStream

fun <T> List<(T) -> Unit>.invokeAll(value: T) {
    var i = 0
    while (i < size) {
        get(i)(value)
        i++
    }
}

inline fun <T, R> List<T>.mapMutable(transform: (T) -> R): MutableList<R> {
    val list = ArrayList<R>(this.size)
    for (it in this)
        list.add(transform(it))
    return list
}

inline val Node.operation
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

typealias Klass = Class<*>

/**
 * Получает класс по имени / дескриптору
 */
fun klassOf(name: String): Klass =
    if (name.isPrimitive())
        name.getPrimitive()
    else Class.forName(name.let { if (name.startsWith('L') && name.endsWith(';')) name.substring(1, name.length - 1).replace('/', '.') else name }) as Klass


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
