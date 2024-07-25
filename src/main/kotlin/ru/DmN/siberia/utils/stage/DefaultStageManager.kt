package ru.DmN.siberia.utils.stage

class DefaultStageManager(
    private var stage: IStage,
    private val stages: MutableList<IStage>
) : StageManager() {
    private val tasks: MutableMap<IStage, MutableList<Runnable>> = HashMap()

    override fun addStageBefore(stage: IStage, other: IStage) {
        var i = 0
        while (i < stages.size) {
            if (stages[i] == other)
                break
            i++
        }
        stages.add(i, stage)
    }

    override fun addStageAfter(stage: IStage, other: IStage) {
        var i = 0
        while (i < stages.size) {
            if (stages[i] == other)
                break
            i++
        }
        stages.add(i + 1, stage)
    }

    override fun getStage(): Pair<Int, IStage> =
        Pair(getPosition(stage), stage)

    override fun getPosition(stage: IStage): Int =
        stages.indexOf(stage)

    override fun pushTask(stage: IStage, task: Runnable) {
        tasks.getOrPut(stage) { ArrayList() }.add(task)
    }

    override fun runAll() =
        stages.asSequence().mapNotNull(tasks::get).forEach { it.forEach(Runnable::run) }
}