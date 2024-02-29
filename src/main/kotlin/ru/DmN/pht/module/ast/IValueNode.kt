package ru.DmN.pht.std.module.ast

import ru.DmN.siberia.ast.Node

interface IValueNode : Node {
    val value: Any?
}