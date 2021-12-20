/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.ast

import tech.antibytes.banana.BananaContract.Node

internal data class TextNode(
    val chunks: List<String>
) : Node

internal data class VariableNode(
    val id: String
) : Node

internal data class FunctionNode(
    val id: String,
    val arguments: List<Node> = emptyList()
) : Node

internal data class LinkNode(
    val target: List<Node>,
    val display: List<Node> = emptyList()
) : Node

internal data class FreeLinkNode(
    val url: String,
    val display: List<Node> = emptyList()
) : Node

internal data class CompoundNode(
    val children: List<Node>
) : Node
