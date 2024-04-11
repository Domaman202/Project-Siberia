package ru.DmN.siberia.utils.exception

import ru.DmN.siberia.utils.node.INodeInfo

open class CompilationException(prev: BaseException?, info: INodeInfo) : NodeInfoException(prev, info)