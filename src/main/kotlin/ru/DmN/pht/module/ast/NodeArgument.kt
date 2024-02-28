package ru.DmN.pht.std.module.ast

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.utils.node.INodeInfo

class NodeArgument(override val info: INodeInfo, val name: String, override val value: Any?) : Node(), IValueNode