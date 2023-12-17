package ru.DmN.pht.std.module.ast

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.node.INodeInfo

class NodeArgument(info: INodeInfo, val name: String, override val value: Any?) : Node(info), IValueNode