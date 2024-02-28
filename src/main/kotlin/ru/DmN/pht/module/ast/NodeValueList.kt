package ru.DmN.pht.std.module.ast

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.utils.node.INodeInfo

class NodeValueList(override val info: INodeInfo, override val value: List<Any?>) : Node(), IValueNode