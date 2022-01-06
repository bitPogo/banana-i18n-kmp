/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.mock.interpreter.InterpreterControllerStub
import tech.antibytes.mock.interpreter.LinkFormatterStub
import tech.antibytes.mock.interpreter.NodeConcatenatorStub
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fixture.listFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class LinkInterpreterSpec {
    private val fixture = kotlinFixture()
    private val concatenator = NodeConcatenatorStub()
    private val controller = InterpreterControllerStub()
    private val formatter = LinkFormatterStub()

    @AfterTest
    fun tearDown() {
        concatenator.clear()
        controller.clear()
        formatter.clear()
    }

    @Test
    fun `It fulfils ParameterizedInterpreterPlugin`() {
        LinkInterpreter(concatenator, formatter) fulfils PublicApi.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given interprete is called with a LinkNode and a InterpreterController, it delegates its wraped TargetNodes and the Controller to the Concartenator`() {
        // Given
        val node = CoreNode.LinkNode(
            target = listOf(
                CoreNode.TextNode(fixture.listFixture()),
                CoreNode.VariableNode(fixture.fixture()),
                CoreNode.FunctionNode(fixture.fixture())
            )
        )

        val capturedNodes: MutableList<PublicApi.Node> = mutableListOf()
        var capturedController: PublicApi.InterpreterController? = null

        concatenator.concatenate = { givenNodes, givenController ->
            capturedNodes.addAll(givenNodes)
            capturedController = givenController
            ""
        }

        // When
        LinkInterpreter(concatenator, formatter).interpret(node, controller)

        // Then
        capturedNodes mustBe node.target
        capturedController!! mustBe controller
    }

    @Test
    fun `Given interprete is called with a LinkNode and a InterpreterController, it delegates its wraped DisplayTextNodes and the Controller to the Concartenator`() {
        // Given
        val node = CoreNode.LinkNode(
            target = emptyList(),
            display = listOf(
                CoreNode.TextNode(fixture.listFixture()),
                CoreNode.VariableNode(fixture.fixture()),
                CoreNode.FunctionNode(fixture.fixture())
            )
        )

        var capturedNodes: List<PublicApi.Node> = emptyList()
        var capturedController: PublicApi.InterpreterController? = null

        concatenator.concatenate = { givenNodes, givenController ->
            capturedNodes = givenNodes
            capturedController = givenController
            ""
        }

        // When
        LinkInterpreter(concatenator, formatter).interpret(node, controller)

        // Then
        capturedNodes mustBe node.display
        capturedController!! mustBe controller
    }

    @Test
    fun `Given interprete is called with a LinkNode and a InterpreterController, it delegates the interpreted Nodes to the Formatter and returns the result`() {
        // Given
        val target: String = fixture.fixture()
        val display: String = fixture.fixture()
        val expected: String = fixture.fixture()

        val interpreted = mutableListOf(target, display)

        var capturedTarget: String? = null
        var capturedDisplay: String? = null

        concatenator.concatenate = { _, _ ->
            interpreted.removeFirst()
        }

        formatter.formatLink = { givenTarget, givenDisplay ->
            capturedTarget = givenTarget
            capturedDisplay = givenDisplay
            expected
        }

        val node = CoreNode.LinkNode(target = emptyList())

        // When
        val result = LinkInterpreter(concatenator, formatter).interpret(node, controller)

        // Then
        result mustBe expected
        capturedTarget!! mustBe target
        capturedDisplay!! mustBe display
    }
}
