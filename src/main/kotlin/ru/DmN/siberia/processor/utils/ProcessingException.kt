package ru.DmN.siberia.processor.utils

import ru.DmN.siberia.utils.exception.BaseException
import ru.DmN.siberia.utils.exception.NodeInfoException
import ru.DmN.siberia.utils.node.INodeInfo

open class ProcessingException(prev: BaseException?, info: INodeInfo) : NodeInfoException(prev, info)