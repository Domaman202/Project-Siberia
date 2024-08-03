package ru.DmN.siberia.utils.exception

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.compiler.Compiler
import ru.DmN.siberia.compiler.utils.CompilationException
import ru.DmN.siberia.compiler.utils.CompilingStage
import ru.DmN.siberia.parser.utils.ParsingException
import ru.DmN.siberia.processor.Processor
import ru.DmN.siberia.processor.utils.ProcessingException
import ru.DmN.siberia.processor.utils.ProcessingStage
import ru.DmN.siberia.unparser.utils.UnparsingException
import ru.DmN.siberia.utils.node.INodeInfo
import ru.DmN.siberia.utils.stage.IStage

inline fun <T> parsingCatcher(info: INodeInfo, block: () -> T): T {
    try {
        return block()
    } catch (e: BaseException) {
        throw ParsingException(e, info)
    } catch (e: NoCatherWrappedException) {
        throw if ((e.i--) > 0) e.cause else e
    } catch (t: Throwable) {
        throw ParsingException(DecoratedException(t), info)
    }
}

inline fun <T> unparsingCatcher(node: Node, block: () -> T): T {
    try {
        return block()
    } catch (e: BaseException) {
        throw UnparsingException(e, node.info)
    } catch (e: NoCatherWrappedException) {
        throw if ((e.i--) > 0) e.cause else e
    } catch (t: Throwable) {
        throw UnparsingException(DecoratedException(t), node.info)
    }
}

inline fun <T> processingCatcher(node: Node, block: () -> T): T {
    try {
        return block()
    } catch (e: BaseException) {
        throw ProcessingException(e, node.info)
    } catch (e: NoCatherWrappedException) {
        throw if ((e.i--) > 0) e.cause else e
    } catch (t: Throwable) {
        throw ProcessingException(DecoratedException(t), node.info)
    }
}

inline fun <T> compilationCatcher(node: Node, block: () -> T): T {
    try {
        return block()
    } catch (e: BaseException) {
        throw CompilationException(e, node.info)
    } catch (e: NoCatherWrappedException) {
        throw if ((e.i--) > 0) e.cause else e
    } catch (t: Throwable) {
        throw CompilationException(DecoratedException(t), node.info)
    }
}

inline fun Processor.pushTask(stage: IStage, node: Node, crossinline block: () -> Unit) {
    this.sm.pushTask(stage) {
        processingCatcher(node) {
            block()
        }
    }
}

inline fun Processor.pushOrRunTask(stage: IStage, node: Node, crossinline block: () -> Unit) {
    this.sm.pushOrRunTask(stage) {
        processingCatcher(node) {
            block()
        }
    }
}

inline fun Compiler.pushTask(stage: IStage, node: Node, crossinline block: () -> Unit) {
    this.sm.pushTask(stage) {
        processingCatcher(node) {
            block()
        }
    }
}

inline fun Compiler.pushOrRunTask(stage: IStage, node: Node, crossinline block: () -> Unit) {
    this.sm.pushOrRunTask(stage) {
        processingCatcher(node) {
            block()
        }
    }
}