package ru.DmN.siberia.ctx

/**
 * Коллекция контекстов.
 */
interface IContextCollection<T : IContextCollection<T>> {
    /**
     * Контексты.
     */
    val contexts: MutableMap<IContextKey, Any?>

    /**
     * Создаёт под-коллекцию с новым контекстом.
     *
     * @param key Ключ нового контекста.
     * @param ctx Новый контекст.
     */
    fun with(key: IContextKey, ctx: Any?): T
}