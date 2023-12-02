package ru.DmN.siberia.tests

import ru.DmN.siberia.utils.TypesProvider
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal class VirtualTypeTests {
    private val vtList = TypesProvider.JAVA.typeOf("java.util.List")
    private val mAdd = vtList.methods.find { it.name == "add" }!!
    private val mGet = vtList.methods.find { it.name == "get" }!!

    @Test
    fun testGenerics() {
        assertTrue(vtList.generics.isNotEmpty())
        assertEquals(vtList.generics, listOf("E"))
        assertFalse(mAdd.genericRettype)
        assertTrue(mGet.genericRettype)
    }
}