package ru.DmN.siberia.test

import org.junit.jupiter.api.Assertions.assertNotEquals
import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Siberia
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.utils.Module
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class ModuleTests {
    @Test
    fun parse() {
        assertNull(Module["test/module/parse"])
        Parser(Module.getModuleFile("test/module/parse")).parseNode(ParsingContext.of(Siberia, StdModule))
        val module = Module["test/module/parse"]
        assertNotNull(module)
        assertEquals(module.name, "test/module/parse")
        assertNotEquals(module.version, "0.0.0")
        assertEquals(module.version, "1.0.0")
        assertNotEquals(module.author, "unknown")
        assertEquals(module.author, "DomamaN202")
        assertEquals(module.deps, listOf("siberia"))
        assertEquals(module.uses, listOf("siberia", "test/module/parse"))
    }
}