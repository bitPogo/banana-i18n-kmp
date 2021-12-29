/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.ast

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Node

private val voidFunctionArguments = CoreNodes.CompoundNode(emptyList())

sealed class CoreNodes : Node {
    data class TextNode(
        val chunks: List<String>
    ) : CoreNodes()

    data class VariableNode(
        val id: String
    ) : CoreNodes()

    data class FunctionNode(
        val id: String,
        val arguments: Node = voidFunctionArguments
    ) : CoreNodes()

    data class LinkNode(
        val target: List<Node>,
        val display: List<Node> = emptyList()
    ) : CoreNodes()

    data class FreeLinkNode(
        val url: Node,
        val display: List<Node> = emptyList()
    ) : CoreNodes()

    data class CompoundNode(
        val children: List<Node>
    ) : Node {
        internal companion object : BananaContract.NodeFactory {
            override fun createNode(children: List<Node>): Node = CompoundNode(children)
        }
    }
}
