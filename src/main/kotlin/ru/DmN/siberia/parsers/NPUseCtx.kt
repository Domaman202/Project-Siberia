package ru.DmN.siberia.parsers

import ru.DmN.pht.module.utils.getOrLoadModule
import ru.DmN.siberia.Parser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.lexer.Token
import ru.DmN.siberia.utils.node.INodeInfo
import ru.DmN.siberia.utils.node.NodeTypes
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.utils.IPlatform
import ru.DmN.pht.module.utils.Module
import ru.DmN.pht.module.utils.ModulesProvider

object NPUseCtx : INodeParser {
    override fun parse(parser: Parser, ctx: ParsingContext, token: Token): Node {
        val names = ArrayList<String>()
        var tk = parser.nextToken()!!
        while (tk.type == Token.DefaultType.OPERATION) {
            names.add(tk.text!!)
            tk = parser.nextToken()!!
        }
        parser.tokens.push(tk)
        return parser.mp.parse(names, parser, ctx) { context ->
            NPProgn.parse(parser, context) { NodeUse(INodeInfo.of(NodeTypes.USE_CTX, ctx, token), it, names) }
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
    fun ModulesProvider.parse(names: MutableList<String>, parser: Parser, ctx: ParsingContext, parse: (context: ParsingContext) -> Node): Node {
        val context = ctx.subCtx()
        return injectModules(
            context.platform,
            context.loadedModules,
            names,
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
    fun ModulesProvider.loadModules(names: MutableList<String>, parser: Parser, ctx: ParsingContext) =
        getModules(names, ctx.platform).forEach { it.load(parser, ctx, names) }

    /**
     * Получает список модулей по их имени.
     * Парсит модули при необходимости.
     *
     * @param names Имена модулей.
     * @return Список модулей.
     */
    fun ModulesProvider.getModules(names: List<String>, platform: IPlatform): List<Module> =
        names.map { getOrLoadModule(it, platform)}

    /**
     * Получает список модулей по их имени.
     * Парсит модули при необходимости.
     *
     * @param names Имена модулей.
     * @return Список модулей.
     */
    fun ModulesProvider.getModules(names: Sequence<String>, platform: IPlatform): Sequence<Module> =
        names.map { getOrLoadModule(it, platform) }

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
    private inline fun <T> ModulesProvider.injectModules(platform: IPlatform, modules: MutableList<Module>, uses: List<String>, load: (Module) -> Unit, clean: (Module) -> Unit, block: () -> T): T {
        val loaded = ArrayList<Module>(uses.size)
        var i = 0
        while (i < uses.size) {
            val name = uses[i++]
            if (modules.any { it.name == name })
                continue
            val module = getOrLoadModule(name, platform)
            load(module)
            loaded += module
        }
        val result = block()
        loaded.forEach(clean)
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
