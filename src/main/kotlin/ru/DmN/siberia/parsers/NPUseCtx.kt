package ru.DmN.siberia.parsers

import ru.DmN.pht.module.utils.getOrLoadModule
import ru.DmN.pht.std.module.StdModule
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Siberia
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.utils.Module

object NPUseCtx : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val names = ArrayList<String>()
        var tk = parser.nextToken()!!
        while (tk.type == Token.DefaultType.OPERATION) {
            names.add(tk.text!!)
            tk = parser.nextToken()!!
        }
        parser.tokens.push(tk)
        return parse(names, parser, ctx) { context ->
            NPProgn.parse(parser, context) { NodeUse(INodeInfo.of(NodeTypes.USE_CTX, ctx, token), names, it) }
        }
    }

    /**
     * Вспомогательная функция, для парсинга "use-ctx подобных" нод.
     *
     * @param names Список модулей.
     * @param parser Парсер.
     * @param ctx Контекст парсинга.
     * @param parse Метод создающий ноду.
     * @return Созданная нода.
     */
    fun parse(names: MutableList<String>, parser: Parser, ctx: ParsingContext, parse: (context: ParsingContext) -> Node): Node {
        val context = ctx.subCtx()
        return injectModules(
            context.loadedModules,
            getModules(names),
            { it.load(parser, context, names) },
            { it.clear(parser, context) },
            { parse(context) }
        )
    }

    /**
     * Загружает модули в контекст парсинга.
     *
     * @param names Имена модулей.
     */
    fun loadModules(names: MutableList<String>, parser: Parser, ctx: ParsingContext) =
        getModules(names).forEach { it.load(parser, ctx, names) }

    /**
     * Получает список модулей по их имени.
     * Парсит модули при необходимости.
     *
     * @param names Имена модулей.
     * @return Список модулей.
     */
    fun getModules(names: List<String>): List<Module> =
        names.map(::getOrLoadModule)

    /**
     * Получает список модулей по их имени.
     * Парсит модули при необходимости.
     *
     * @param names Имена модулей.
     * @return Список модулей.
     */
    fun getModules(names: Sequence<String>): Sequence<Module> =
        names.map(::getOrLoadModule)

    /**
     * Загружает незагруженные модули.
     * Выполняет код блока, после выгружает модули.
     *
     * @param modules Список загруженных модулей.
     * @param uses Список необходимых модулей.
     * @param clean Очистка модулей.
     * @param block Блок.
     * @return Результат выполнения блока.
     */
    private inline fun <T> injectModules(modules: MutableList<Module>, uses: List<Module>, load: (Module) -> Unit, clean: (Module) -> Unit, block: () -> T): T {
        val new = uses.filter { !modules.contains(it) }.onEach(load)
        val result = block()
        new.forEach(clean)
        return result
    }

    /*
    fun <T> injectModules(modules: MutableList<Module>, uses: List<Module>, load: (Module) -> Unit, clean: (Module) -> Unit, block: () -> T): T {
        val oldUses = ArrayList<Pair<Module, Int>>()
        val newUses = ArrayList<Module>()
        uses.forEach {
            if (modules.contains(it))
                oldUses += Pair(it, modules.indexOf(it))
            else newUses += it
        }
        oldUses.forEach { modules.removeAt(it.second); modules.add(0, it.first) }
        newUses.forEach { load(it) }
        val result = block()
        (0 until oldUses.size + newUses.size).forEach { _ -> modules.removeAt(0) }
        oldUses.forEach { modules.add(it.second, it.first) }
        newUses.forEach(clean)
        return result
     */
}
