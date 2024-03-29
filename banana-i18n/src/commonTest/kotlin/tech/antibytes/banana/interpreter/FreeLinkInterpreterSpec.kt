/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import kotlin.test.AfterTest
import kotlin.test.Test
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.mock.interpreter.InterpreterControllerStub
import tech.antibytes.mock.interpreter.LinkFormatterStub
import tech.antibytes.mock.interpreter.NodeConcatenatorStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class FreeLinkInterpreterSpec {
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
        FreeLinkInterpreter(concatenator, formatter) fulfils PublicApi.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given interprete is called with a FreeLinkNode and a InterpreterController, it delegates its wraped Url to the Controller`() {
        // Given
        val node = CoreNode.FreeLinkNode(
            url = CoreNode.TextNode(fixture.listFixture()),
        )

        var capturedNode: PublicApi.Node? = null

        controller.interpret = { givenNode ->
            capturedNode = givenNode
            ""
        }

        // When
        FreeLinkInterpreter(concatenator, formatter).interpret(node, controller)

        // Then
        capturedNode!! mustBe node.url
    }

    @Test
    fun `Given interprete is called with a FreeLinkNode and a InterpreterController, it delegates its wraped DisplayTextNodes and the Controller to the Concartenator`() {
        // Given
        val node = CoreNode.FreeLinkNode(
            url = CoreNode.TextNode(fixture.listFixture()),
            display = listOf(
                CoreNode.TextNode(fixture.listFixture()),
                CoreNode.VariableNode(fixture.fixture()),
                CoreNode.FunctionNode(fixture.fixture()),
            ),
        )

        var capturedNodes: List<PublicApi.Node> = emptyList()
        var capturedController: PublicApi.InterpreterController? = null

        controller.interpret = { _ -> "" }
        concatenator.concatenate = { givenNodes, givenController ->
            capturedNodes = givenNodes
            capturedController = givenController
            ""
        }

        // When
        FreeLinkInterpreter(concatenator, formatter).interpret(node, controller)

        // Then
        capturedNodes mustBe node.display
        capturedController!! mustBe controller
    }

    @Test
    fun `Given interprete is called with a FreeLinkNode and a InterpreterController, it delegates the interpreted Nodes to the Formatter and returns the result`() {
        // Given
        val url: String = fixture.fixture()
        val display: String = fixture.fixture()
        val expected: String = fixture.fixture()
        var capturedUrl: String? = null
        var capturedDisplay: String? = null

        controller.interpret = { _ ->
            url
        }

        concatenator.concatenate = { _, _ ->
            display
        }

        formatter.formatFreeLink = { givenTarget, givenDisplay ->
            capturedUrl = givenTarget
            capturedDisplay = givenDisplay
            expected
        }

        val node = CoreNode.FreeLinkNode(url = CoreNode.TextNode(fixture.listFixture()))

        // When
        val result = FreeLinkInterpreter(concatenator, formatter).interpret(node, controller)

        // Then
        result mustBe expected
        capturedUrl!! mustBe url
        capturedDisplay!! mustBe display
    }
}
