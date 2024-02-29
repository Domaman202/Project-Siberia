package ru.DmN.pht.std.module.ast

import ru.DmN.siberia.ast.BaseNode
import ru.DmN.siberia.utils.node.INodeInfo

class NodeValue(info: INodeInfo, override val value: String) : BaseNode(info), IValueNode