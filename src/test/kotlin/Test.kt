import ru.DmN.siberia.utils.stage.DefaultStageManager
import ru.DmN.siberia.utils.stage.IStage
import ru.DmN.siberia.utils.stage.StageManager

object Test {
    enum class Stages : IStage {
        ONE,
        TWO,
        THREE
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val manager: StageManager = DefaultStageManager(Stages.ONE, mutableListOf(Stages.ONE))
        manager.addStageAfter(Stages.TWO, Stages.ONE)
        manager.addStageBefore(Stages.THREE, Stages.TWO)
        manager.pushTask(Stages.ONE) { println("ONE") }
        manager.pushTask(Stages.TWO) { println("TWO") }
        manager.pushTask(Stages.THREE) { println("THREE") }
        manager.runAll()
    }
}