package ru.DmN.siberia.unparser

import ru.DmN.pht.module.utils.ModulesProvider
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.unparsers.INodeUnparser

/**
 * Абстрактный де-парсер.
 */
abstract class Unparser {
    /**
     * Поставщик модулей.
     */
    abstract val mp: ModulesProvider

    /**
     * Выход
     */
    abstract val out: StringBuilder

    /**
     * Де-парсит ноду.
     *
     * @param indent Отступ табуляций.
     */
    abstract fun unparse(node: Node, ctx: UnparsingContext, indent: Int)

    /**
     * Возвращает де-парсер нод.
     */
    abstract fun get(ctx: UnparsingContext, node: Node): INodeUnparser<Node>
}