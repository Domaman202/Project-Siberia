package ru.DmN.siberia.utils

/**
 * Менеджер стадий.
 */
abstract class StageManager<S : Enum<S>> {
    /**
     * Получает стадию.
     *
     * @return [Порядковый номер; Стадия]
     */
    abstract fun getStage(): Pair<Int, S>

    /**
     * Получает порядковый номер стадии.
     */
    abstract fun getPosition(stage: S): Int

    /**
     * Добавляет новую задачу.
     */
    abstract fun pushTask(stage: S, task: Runnable)

    /**
     * Возвращает Sequence со всеми стадиями.
     */
    abstract fun asSequence(): Sequence<Pair<S, Sequence<Runnable>>>

    /**
     * Запускает все стадии.
     */
    abstract fun runAll()
}