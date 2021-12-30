/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.ast

import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.PublicApi.Node

private val voidFunctionArguments = CoreNode.CompoundNode(emptyList())

sealed class CoreNode : Node {
    data class TextNode(
        val chunks: List<String>
    ) : CoreNode()

    data class VariableNode(
        val id: String
    ) : CoreNode()

    data class FunctionNode(
        val id: String,
        val arguments: Node = voidFunctionArguments
    ) : CoreNode()

    data class LinkNode(
        val target: List<Node>,
        val display: List<Node> = emptyList()
    ) : CoreNode()

    data class FreeLinkNode(
        val url: Node,
        val display: List<Node> = emptyList()
    ) : CoreNode()

    data class CompoundNode(
        val children: List<Node>
    ) : CoreNode() {
        internal companion object : PublicApi.NodeFactory {
            override fun createNode(children: List<Node>): Node = CompoundNode(children)
        }
    }
}
