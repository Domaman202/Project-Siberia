package ru.DmN.pht.std.module.ast

import ru.DmN.siberia.ast.Node
import ru.DmN.siberia.node.INodeInfo

class NodeValue(info: INodeInfo, override val value: String) : Node(info), IValueNode