package ru.DmN.siberia.parser.utils

import ru.DmN.siberia.utils.exception.BaseException
import ru.DmN.siberia.utils.exception.NodeInfoException
import ru.DmN.siberia.utils.node.INodeInfo

open class ParsingException(prev: BaseException?, info: INodeInfo) : NodeInfoException(prev, info)