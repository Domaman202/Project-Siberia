package ru.DmN.siberia.compiler.ctx

import ru.DmN.siberia.Siberia
import ru.DmN.siberia.compiler.utils.withJCV
import ru.DmN.siberia.processor.utils.Platform
import ru.DmN.siberia.processor.utils.with
import ru.DmN.siberia.utils.*

/**
 * Контекст компиляции.
 */
class CompilationContext(
    /**
     * Загруженные модули.
     */
    val loadedModules: MutableList<Module> = ArrayList(),
    /**
     * Контексты
     */
    override val contexts: MutableMap<String, Any?> = HashMap()
) : IContextCollection<CompilationContext> {
    /**
     * Создаёт под-контекст.
     */
    fun subCtx() =
        CompilationContext(SubList(loadedModules), SubMap(contexts))

    /**
     * Создаёт под-контекст с общими модульными зависимостями.
     * Добавляет новый элемент контекста.
     *
     * @param name Имя нового элемента контекста.
     * @param ctx Новый элемент контекста.
     */
    override fun with(name: String, ctx: Any?): CompilationContext =
        CompilationContext(loadedModules, contexts.toMutableMap().apply { this[name] = ctx })

    companion object {
        /**
         * Создаёт базовый контекст.
         */
        fun base(): CompilationContext =
            CompilationContext(mutableListOf(Siberia)).with(Platform.JAVA).withJCV(getJavaClassVersion())
    }
}