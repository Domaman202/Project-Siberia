package ru.DmN.siberia.compiler.utils

import ru.DmN.siberia.utils.exception.BaseException
import ru.DmN.siberia.utils.exception.NodeInfoException
import ru.DmN.siberia.utils.node.INodeInfo

open class CompilationException(prev: BaseException?, info: INodeInfo) : NodeInfoException(prev, info)