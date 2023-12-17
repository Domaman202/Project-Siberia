package ru.DmN.pht.std.module.ast

import ru.DmN.siberia.ast.NodeNodesList
import ru.DmN.siberia.node.INodeInfo
import ru.DmN.siberia.utils.Module

class NodeModule(info: INodeInfo, val data: Map<String, Any?>) : NodeNodesList(info) {
    lateinit var module: Module
}