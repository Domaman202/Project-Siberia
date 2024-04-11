package ru.DmN.siberia.utils.exception

import ru.DmN.siberia.utils.node.INodeInfo

open class ProcessingException(prev: BaseException?, info: INodeInfo) : NodeInfoException(prev, info)