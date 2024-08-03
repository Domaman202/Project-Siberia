package ru.DmN.siberia.utils.stage

import ru.DmN.siberia.utils.safeForEach

class DefaultStageManager(
    private var stage: IStage,
    val stages: MutableList<IStage>
) : StageManager() {
    private val tasks: MutableMap<IStage, MutableList<Runnable>> = HashMap()

    override fun addStageBefore(stage: IStage, other: IStage) {
        var i = 0
        while (i < this.stages.size) {
            if (this.stages[i] == other)
                break
            i++
        }
        this.stages.add(i, stage)
    }

    override fun addStageAfter(stage: IStage, other: IStage) {
        var i = 0
        while (i < this.stages.size) {
            if (this.stages[i] == other)
                break
            i++
        }
        this.stages.add(i + 1, stage)
    }

    override fun containsStage(stage: IStage): Boolean =
        this.stages.contains(stage)

    override fun getStage(): Pair<Int, IStage> =
        Pair(getPosition(this.stage), this.stage)

    override fun getPosition(stage: IStage): Int =
        this.stages.indexOf(stage)

    override fun pushTask(stage: IStage, task: Runnable) {
        this.tasks.getOrPut(if (getPosition(stage) <= getPosition(this.stage)) this.stage else stage) { ArrayList() } += task
    }

    override fun pushOrRunTask(stage: IStage, task: Runnable) {
        if (getPosition(stage) <= getPosition(this.stage))
            task.run()
        else this.tasks.getOrPut(stage) { ArrayList() } += task
    }

    override fun runAll() {
        this.stages.safeForEach {
            this.stage = it
            this.tasks[it]?.safeForEach(Runnable::run)
        }
    }
}