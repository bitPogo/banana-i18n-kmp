/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import kotlin.test.Test
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.mock.interpreter.InterpreterControllerStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

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
            CoreNode.TextNode(fixture.listFixture()),
            CoreNode.VariableNode(fixture.fixture()),
            CoreNode.FunctionNode(fixture.fixture()),
        )

        val capturedNodes: MutableList<PublicApi.Node> = mutableListOf()

        controller.interpret = { givenNode ->
            capturedNodes.add(givenNode)
            fixture.fixture()
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
            CoreNode.TextNode(fixture.listFixture()),
            CoreNode.VariableNode(fixture.fixture()),
            CoreNode.FunctionNode(fixture.fixture()),
        )

        val output: MutableList<String> = mutableListOf(
            fixture.fixture(),
            fixture.fixture(),
            fixture.fixture(),
        )

        println(output)

        val expected = output.joinToString("")

        controller.interpret = { output.removeFirst() }

        // When
        val actual = NodeConcatenator().concatenate(nodes, controller)

        // Then
        actual mustBe expected
    }
}
