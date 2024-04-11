package ru.DmN.siberia.unparser.utils

import ru.DmN.siberia.utils.exception.BaseException
import ru.DmN.siberia.utils.exception.NodeInfoException
import ru.DmN.siberia.utils.node.INodeInfo

open class UnparsingException(prev: BaseException?, info: INodeInfo) : NodeInfoException(prev, info)