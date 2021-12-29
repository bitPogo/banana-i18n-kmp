/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNodes
import tech.antibytes.mock.interpreter.InterpreterControllerStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class NodeConcatenatorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils NodeConcartinator`() {
        NodeConcatenator() fulfils BananaContract.NodeConcatenator::class
    }

    @Test
    fun `Given concartinate is called with a List of Nodes and a InterpreterController it delegates the nodes to the Controller`() {
        // Given
        val controller = InterpreterControllerStub()
        val nodes = listOf(
            CoreNodes.TextNode(fixture()),
            CoreNodes.VariableNode(fixture()),
            CoreNodes.FunctionNode(fixture())
        )

        val capturedNodes: MutableList<BananaContract.Node> = mutableListOf()

        controller.interpret = { givenNode ->
            capturedNodes.add(givenNode)
            fixture()
        }

        // When
        NodeConcatenator().concatenate(nodes, controller)

        // Then
        capturedNodes mustBe nodes
    }

    @Test
    fun `Given concartinate is called with a List of Nodes and a InterpreterController it returns accumulated the interpreted Nodes`() {
        // Given
        val controller = InterpreterControllerStub()
        val nodes = listOf(
            CoreNodes.TextNode(fixture()),
            CoreNodes.VariableNode(fixture()),
            CoreNodes.FunctionNode(fixture())
        )

        val output: MutableList<String> = mutableListOf(
            fixture(),
            fixture(),
            fixture()
        )

        val expected = output.joinToString("")

        controller.interpret = { output.removeFirst() }

        // When
        val actual = NodeConcatenator().concatenate(nodes, controller)

        // Then
        actual mustBe expected
    }
}
