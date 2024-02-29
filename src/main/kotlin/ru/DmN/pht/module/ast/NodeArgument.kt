package ru.DmN.pht.std.module.ast

import ru.DmN.siberia.ast.BaseNode
import ru.DmN.siberia.utils.node.INodeInfo

class NodeArgument(info: INodeInfo, val name: String, override val value: Any?) : BaseNode(info), IValueNode