package ru.DmN.siberia.unparsers

import ru.DmN.siberia.Unparser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.unparser.UnparsingContext

/**
 * Де-парсер нод.
 */
interface INodeUnparser<T : Node> {
    /**
     * Де-парсит ноду.
     *
     * @param indent Отступ табуляций.
     * @param line Нода парситься в одной строке? (Нужно для того, чтобы в случае длинной ноды она переносилась на новую строку)
     */
    fun unparse(node: T, unparser: Unparser, ctx: UnparsingContext, indent: Int, line: Boolean)
}