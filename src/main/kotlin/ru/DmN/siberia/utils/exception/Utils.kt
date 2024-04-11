package ru.DmN.siberia.utils.exception

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.Compiler
import ru.DmN.siberia.compiler.utils.CompilingStage
import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.processor.utils.ProcessingStage
import ru.DmN.siberia.utils.node.INodeInfo

inline fun <T> parsingCatcher(info: INodeInfo, block: () -> T): T {
    try {
        return block()
    } catch (e: BaseException) {
        throw ParsingException(e, info)
    } catch (t: Throwable) {
        throw ParsingException(DecoratedException(t), info)
    }
}

inline fun <T> unparsingCatcher(node: Node, block: () -> T): T {
    try {
        return block()
    } catch (e: BaseException) {
        throw UnparsingException(e, node.info)
    } catch (t: Throwable) {
        throw UnparsingException(DecoratedException(t), node.info)
    }
}

inline fun <T> processingCatcher(node: Node, block: () -> T): T {
    try {
        return block()
    } catch (e: BaseException) {
        throw ProcessingException(e, node.info)
    } catch (t: Throwable) {
        throw ProcessingException(DecoratedException(t), node.info)
    }
}

inline fun <T> compilationCatcher(node: Node, block: () -> T): T {
    try {
        return block()
    } catch (e: BaseException) {
        throw CompilationException(e, node.info)
    } catch (t: Throwable) {
        throw CompilationException(DecoratedException(t), node.info)
    }
}

inline fun Processor.pushTask(stage: ProcessingStage, node: Node, crossinline block: () -> Unit) {
    this.stageManager.pushTask(stage) {
        processingCatcher(node) {
            block()
        }
    }
}

inline fun Compiler.pushTask(stage: CompilingStage, node: Node, crossinline block: () -> Unit) {
    this.stageManager.pushTask(stage) {
        processingCatcher(node) {
            block()
        }
    }
}