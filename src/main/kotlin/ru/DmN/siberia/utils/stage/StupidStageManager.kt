package ru.DmN.siberia.utils.stage

import ru.DmN.siberia.utils.DefaultEnumMap

/**
 * Stupid Stage Manager
 */
class StupidStageManager<S : Enum<S>>(
    /**
     * Класс стадий.
     */
    stageType: Class<S>,
    /**
     * Текущая стадия.
     */
    private var stage: S,
    /**
     * Список стадий.
     */
    private var stages: Array<S>
) : StageManager<S>() {
    /**
     * Список задач.
     */
    private val tasks: DefaultEnumMap<S, MutableList<Runnable>> = DefaultEnumMap(stageType) { ArrayList() }

    override fun getStage(): Pair<Int, S> =
        Pair(stage.ordinal, stage)

    override fun getPosition(stage: S): Int =
        stage.ordinal

    override fun pushTask(stage: S, task: Runnable) {
        if (stage.ordinal <= this.stage.ordinal)
            task.run()
        else tasks[stage] += task
    }

    override fun asSequence(): Sequence<Pair<S, Sequence<Runnable>>> =
        tasks.asSequence().map { Pair(it.key, it.value.asSequence()) }

    override fun runAll() {
        stages.forEach {
            stage = it
            tasks[it].forEach(Runnable::run)
        }
    }

    companion object {
        inline fun <reified S : Enum<S>> of(initial: S, stages: Array<S>) =
            StupidStageManager(S::class.java, initial, stages)
    }
}