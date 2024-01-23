package ru.DmN.siberia.utils

import ru.DmN.siberia.Compiler
import ru.DmN.siberia.Parser
import ru.DmN.siberia.Processor
import ru.DmN.siberia.Unparser
import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.node.INodeType
import ru.DmN.siberia.node.NodeInfoImpl
import ru.DmN.siberia.node.NodeTypes
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.parsers.NPUseCtx
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.Platforms
import ru.DmN.siberia.processor.utils.ValType
import ru.DmN.siberia.processor.utils.module
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.unparser.UnparsingContext
import ru.DmN.siberia.unparsers.INodeUnparser
import java.io.File
import java.io.FileNotFoundException
import java.util.*

/**
 * Модуль.
 *
 * @param name Имя модуля.
 */
open class Module(val name: String) {
    /**
     * Модуль инициализирован?
     */
    var init: Boolean = false

    /**
     * Версия модуля.
     */
    var version: String = "0.0.0"

    /**
     * Автор(ы) модуля.
     */
    var author: String = "unknown"

    /**
     * Зависимости модуля.
     */
    var deps: List<String> = ArrayList()

    /**
     * Используемые модули.
     */
    var uses: List<String> = ArrayList()

    /**
     * Файлы исходного кода.
     */
    var sources: List<String> = ArrayList()

    /**
     * Файлы ресурсов.
     */
    var resources: List<String> = ArrayList()

    /**
     * Парсеры.
     */
    val parsers: MutableMap<Regex, INodeParser> = HashMap()

    /**
     * Де-парсеры.
     */
    val unparsers: MutableMap<INodeType, INodeUnparser<*>> = HashMap()

    /**
     * Обработчики.
     */
    val processors: MutableMap<INodeType, INodeProcessor<*>> = HashMap()

    /**
     * Компиляторы.
     */
    val compilers: MutableMap<Platforms, MutableMap<INodeType, INodeCompiler<*>>> = EnumMap(Platforms::class.java)

    /**
     * Ноды.
     *
     * Список нод получается при инициализации модуля, в процессе обработки его исходного кода.
     */
    val nodes: MutableList<Node> = ArrayList()

    /**
     * Экспортируемые ноды.
     *
     * Список экспортируемых нод получается при инициализации модуля, в процессе обработки его исходного кода.
     */
    val exports: MutableList<NodeNodesList> = ArrayList()

    /**
     * Инициализация модуля.
     *
     * Определяет ноды, экспортируемые ноды модуля.
     */
    open fun init(platform: Platforms) {
        if (!init) {
            init = true
            sources.forEach {
                val parser = Parser(getModuleFile(it))
                val pctx = ParsingContext.base().apply { this.module = this@Module; this.platform = platform }
                val uses = uses.toMutableList()
                nodes.add(
                    NPUseCtx.parse(uses, parser, pctx) { context ->
                        NodeUse(
                            NodeInfoImpl(NodeTypes.USE_CTX, null, null),
                            uses,
                            mutableListOf(parser.parseNode(context)!!),
                        )
                    },
                )
            }
        }
    }

    /**
     * Загружает модуль в контекст парсинга.
     *
     * @param parser Парсер.
     * @param ctx Контекст парсинга.
     * @param uses Используемые модули. (Можно добавить свои модули)
     */
    open fun load(parser: Parser, ctx: ParsingContext, uses: MutableList<String>) {
        if (!ctx.loadedModules.contains(this)) {
            ctx.loadedModules.add(0, this)
        }
    }

    /**
     * Очищает контекст парсинга от модуля.
     */
    open fun clear(parser: Parser, ctx: ParsingContext) =
        Unit

    /**
     * Выгружает модуль из контекста парсинга.
     *
     * @param parser Парсер.
     * @param ctx Контекст парсинга.
     */
    open fun unload(parser: Parser, ctx: ParsingContext) {
        if (ctx.loadedModules.contains(this)) {
            ctx.loadedModules.remove(this)
        }
    }

    /**
     * Загружает модуль в контекст де-парсинга.
     *
     * @param unparser Де-парсер.
     * @param ctx Контекст де-парсинга.
     */
    open fun load(unparser: Unparser, ctx: UnparsingContext) {
        if (!ctx.loadedModules.contains(this)) {
            ctx.loadedModules.add(0, this)
        }
    }

    /**
     * Загружает модуль в контекст обработки.
     *
     * @param processor Обработчик.
     * @param ctx Контекст обработки.
     */
    open fun load(processor: Processor, ctx: ProcessingContext, uses: MutableList<String>): Boolean {
        if (!ctx.loadedModules.contains(this)) {
            ctx.loadedModules.add(0, this)
            return true
        }
        return false
    }

    /**
     * Загружает модуль в контекст компиляции.
     *
     * @param compiler Компилятор.
     * @param ctx Контекст компиляции.
     */
    open fun load(compiler: Compiler, ctx: CompilationContext) {
        if (!ctx.loadedModules.contains(this)) {
            ctx.loadedModules.add(0, this)
        }
    }

    /**
     * Читает определённый файл текущего модуля.
     *
     * @param file Файл.
     * @return Данные этого файла (в виде строки).
     */
    fun getModuleFile(file: String): String {
        val stream = Module::class.java.getResourceAsStream("/$name/$file")
        if (stream == null) {
            val f = File("$name/$file")
            if (f.isFile)
                return String(f.readBytes())
            throw FileNotFoundException("/$name/$file")
        }
        return String(stream.readBytes())
    }

    /**
     * Добавляет новый парсер нод.
     *
     * @param pattern Шаблон парсинга.
     * @param parser Парсер.
     */
    fun add(pattern: Regex, parser: INodeParser) {
        parsers[pattern] = parser
    }

    /**
     * Добавляет де-парсер нод.
     *
     * @param type Тип нод.
     * @param unparser Де-парсер.
     */
    fun add(type: INodeType, unparser: INodeUnparser<*>) {
        unparsers[type] = unparser
    }

    /**
     * Добавляет обработчик нод.
     *
     * @param type Тип нод.
     * @param processor Обработчик.
     */
    fun add(type: INodeType, processor: INodeProcessor<*>) {
        processors[type] = processor
    }

    /**
     * Добавляет компилятор нод.
     *
     * @param platform Платформа на которую ориентирован компилятор.
     * @param type Тип нод.
     * @param compiler Компилятор.
     */
    fun add(platform: Platforms, type: INodeType, compiler: INodeCompiler<*>) {
        compilers.getOrPut(platform) { HashMap() }[type] = compiler
    }

    override fun toString(): String =
        "Module[$name]"

    companion object {
        private val MODULES: MutableList<Module> = ArrayList()

        /**
         * Получает модуль, в случае его отсутствия либо выполняет метод для его добавления.
         *
         * @param name Имя модуля.
         * @param put Метод для добавления модуля.
         * @return Модуль.
         */
        fun getOrPut(name: String, put: () -> Module): Module =
            get(name) ?: put().apply { MODULES.add(this) }

        /**
         * Получает модуль, в случае его отсутствия кидает исключение.
         *
         * @param name Имя модуля.
         * @return Модуль.
         */
        fun getOrThrow(name: String) =
            get(name) ?: throw RuntimeException("Module '$name' not founded!")

        /**
         * Получает модуль, в случае его отсутствия возвращает null.
         *
         * @param name Имя модуля.
         * @return Модуль - в случае наличия, в случае отсутствия - null.
         */
        operator fun get(name: String): Module? {
            for (i in 0 until MODULES.size) {
                val module = MODULES[i]
                if (module.name == name) {
                    return module
                }
            }

            return null
        }

        /**
         * Читает данные из заголовка модуля.
         *
         * @param name Имя модуля.
         * @return Данные заголовка.
         */
        fun getModuleFile(name: String): String {
            val stream = Module::class.java.getResourceAsStream("/$name/module.pht")
            if (stream == null) {
                val file = File("$name/module.pht")
                if (file.isFile)
                    return String(file.readBytes())
                throw FileNotFoundException("/$name/module.pht")
            }
            return String(stream.readBytes())
        }

        /**
         * Преобразует имя операции в регулярное выражение.
         */
        fun String.toRegularExpr(): Regex =
            this.replace(".", "\\.")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("\\", "\\\\")
                .replace("?", "\\?")
                .replace("*", "\\*")
                .replace("+", "\\+")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .toRegex()
    }
}