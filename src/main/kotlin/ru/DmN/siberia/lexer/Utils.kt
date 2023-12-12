package ru.DmN.siberia.lexer

import ru.DmN.siberia.lexer.Token.DefaultType.*

fun Token.checkOpenCBracket(): Token = if (isOpenCBracket()) this else throw RuntimeException()
fun Token.checkCloseCBracket(): Token = if (isCloseCBracket()) this else throw RuntimeException()
fun Token.checkOperation(): Token = if (isOperation()) this else throw RuntimeException()
fun Token.checkType(): Token = if (isType()) this else throw RuntimeException()
fun Token.checkNaming(): Token = if (isNaming()) this else throw RuntimeException()

fun Token.isOpenCBracket() = type == OPEN_CBRACKET
fun Token.isCloseCBracket() = type == CLOSE_CBRACKET
fun Token.isOperation() = type == OPERATION
fun Token.isType() = type == PRIMITIVE || type == CLASS
fun Token.isNaming() = type == NAMING