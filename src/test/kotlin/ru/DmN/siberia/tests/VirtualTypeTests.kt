package ru.DmN.siberia.tests

import ru.DmN.siberia.utils.TypesProvider
import ru.DmN.siberia.utils.VirtualType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal class VirtualTypeTests {
    private val vtList = TypesProvider.java().typeOf("java.util.List")
    private val mAdd = vtList.methods.find { it.name == "add" }!!
    private val mGet = vtList.methods.find { it.name == "get" }!!

    @Test
    fun testGenerics() {
        assertTrue(vtList.generics.isNotEmpty())
        assertEquals(vtList.generics, listOf(Pair("E", VirtualType.ofKlass(Any::class.java))))
        assertEquals(mAdd.argsg, listOf("E"))
        assertEquals(mGet.retgen, "E")
    }
}