/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.ast

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.InternalNodes.CompoundNode
import tech.antibytes.banana.ast.InternalNodes.TextNode
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class CompoundNodeSpec {
    @Test
    fun `It fulfils ArgumentsNodeFactory`() {
        CompoundNode fulfils BananaContract.NodeFactory::class
    }

    @Test
    fun `Given createNode is called with a List of nodes, it creates a CompoundNode`() {
        // Given
        val nodes = listOf(TextNode(listOf()))

        // When
        val result = CompoundNode.createNode(nodes)

        // Then
        result mustBe CompoundNode(nodes)
    }
}
