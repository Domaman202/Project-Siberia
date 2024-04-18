package ru.DmN.pht.module.utils

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.ast.NodeUse
import ru.DmN.siberia.compiler.Compiler
import ru.DmN.siberia.compiler.ctx.CompilationContext
import ru.DmN.siberia.compilers.INodeCompiler
import ru.DmN.siberia.parser.Parser
import ru.DmN.siberia.parser.ParserImpl
import ru.DmN.siberia.parser.ctx.ParsingContext
import ru.DmN.siberia.parser.utils.file
import ru.DmN.siberia.parsers.INodeParser
import ru.DmN.siberia.parsers.NPUseCtx.parse
import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.processor.ctx.ProcessingContext
import ru.DmN.siberia.processor.utils.module
import ru.DmN.siberia.processor.utils.platform
import ru.DmN.siberia.processors.INodeProcessor
import ru.DmN.siberia.unparser.Unparser
import ru.DmN.siberia.unparser.ctx.UnparsingContext
import ru.DmN.siberia.unparsers.INodeUnparser
import ru.DmN.siberia.utils.IPlatform
import ru.DmN.siberia.utils.node.INodeType
import ru.DmN.siberia.utils.node.NodeTypes.USE_CTX
import ru.DmN.siberia.utils.readBytes
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.InputStream

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
    val compilers: MutableMap<IPlatform, MutableMap<INodeType, INodeCompiler<*>>> = HashMap()

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
     * @param platform Платформа в которой будет работать модуль.
     * @param mp Поставщик модулей.
     */
    open fun init(platform: IPlatform, mp: ModulesProvider) {
        if (!init) {
            init = true
            sources.asFilesSequence(name).forEach {
                val parser = ParserImpl(String(getModuleFile(it).readBytes()), mp)
                val pctx = ParsingContext.base().apply {
                    this.module = this@Module
                    this.file = "$name/$it"
                    this.platform = platform
                }
                val uses = uses.toMutableList()
                nodes.add(
                    parser.mp.parse(uses, parser, pctx) { parser1, context ->
                        val node = parser1.parseNode(context)!!
                        NodeUse(
                            node.info.withType(USE_CTX),
                            mutableListOf(node),
                            uses
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
     * @return Желает ли модуль изменить парсер?
     */
    open fun load(parser: Parser, ctx: ParsingContext, uses: MutableList<String>): Boolean {
        if (!ctx.loadedModules.contains(this))
            ctx.loadedModules.add(0, this)
        return false
    }

    /**
     * Создание нового парсера для смены.
     *
     * @param parser Парсер.
     * @param ctx Контекст парсинга.
     * @return Новый парсер, null - если модуль не может изменять парсер.
     */
    open fun changeParser(parser: Parser, ctx: ParsingContext): Parser? =
        null

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
    open fun getModuleFile(file: String): InputStream {
        val stream = Module::class.java.getResourceAsStream("/$name/$file")
        if (stream == null) {
            val f = File("$name/$file")
            if (f.isFile)
                return FileInputStream(f)
            throw FileNotFoundException("/$name/$file")
        }
        return stream
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
    fun add(platform: IPlatform, type: INodeType, compiler: INodeCompiler<*>) {
        compilers.getOrPut(platform) { HashMap() }[type] = compiler
    }

    override fun toString(): String =
        "Module[$name]"

    companion object {
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