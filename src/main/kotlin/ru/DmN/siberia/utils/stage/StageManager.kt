package ru.DmN.siberia.utils.stage

/**
 * Менеджер стадий.
 */
abstract class StageManager {
    /**
     * Добавляет новую стадию.
     * Указывается стадия которая идёт после новой.
     *
     * @param stage Новая стадия.
     * @param other Следующая стадия.
     */
    abstract fun addStageBefore(stage: IStage, other: IStage)

    /**
     * Добавляет новую стадию.
     * Указывается стадия которая идёт до новой.
     *
     * @param stage Новая стадия.
     * @param other Предыдущая стадия.
     */
    abstract fun addStageAfter(stage: IStage, other: IStage)

    /**
     * Получает текущую стадию.
     *
     * @return [Порядковый номер; Стадия]
     */
    abstract fun getStage(): Pair<Int, IStage>

    /**
     * Получает порядковый номер стадии.
     *
     * @return Порядковый номер.
     */
    abstract fun getPosition(stage: IStage): Int

    /**
     * Добавляет новую задачу.
     *
     * @param stage Стадия в которой будет запускаться задача.
     * @param task Задача.
     */
    abstract fun pushTask(stage: IStage, task: Runnable)

    /**
     * Добавляет новую задачу, если нужная стадия текущая или уже была пройдена, то запускает её.
     *
     * @param stage Стадия в которой будет запускаться задача.
     * @param task Задача.
     */
    abstract fun pushOrRunTask(stage: IStage, task: Runnable)

    /**
     * Запускает все стадии.
     */
    abstract fun runAll()
}