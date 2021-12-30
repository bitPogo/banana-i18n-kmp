/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.mock.interpreter.InterpreterPluginStub
import tech.antibytes.mock.interpreter.ParameterizedInterpreterPluginStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test
import kotlin.test.assertFailsWith

class InterpreterControllerSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils InterpreterController`() {
        InterpreterController(
            emptyMap(),
            ParameterizedInterpreterPluginStub(),
            InterpreterPluginStub(),
            ParameterizedInterpreterPluginStub(),
            ParameterizedInterpreterPluginStub(),
            ParameterizedInterpreterPluginStub(),
            ParameterizedInterpreterPluginStub()
        ) fulfils BananaContract.InterpreterController::class
    }

    @Test
    fun `Given interpret is called with a Unknown Node it fails`() {
        // Given
        val node = UnknownNode()

        // When
        val actual = assertFailsWith<FatalInterpreterError> {
            InterpreterController(
                parameter = emptyMap(),
                variableInterpreter = ParameterizedInterpreterPluginStub(),
                textInterpreter = InterpreterPluginStub(),
                functionSelector = ParameterizedInterpreterPluginStub(),
                compoundInterpreter = ParameterizedInterpreterPluginStub(),
                linkInterpreter = ParameterizedInterpreterPluginStub(),
                freeLinkInterpreter = ParameterizedInterpreterPluginStub()
            ).interpret(node)
        }

        // Then
        actual.message mustBe "Unknown Node detected!"
    }

    @Test
    fun `Given interpret is called with a TextNode it delegates it to the TextInterpreter`() {
        // Given
        val nestedInterpreter = InterpreterPluginStub<CoreNode.TextNode>()
        val expected: String = fixture()
        val node = CoreNode.TextNode(fixture())
        var capturedNode: BananaContract.Node? = null

        nestedInterpreter.interpret = { givenNode ->
            capturedNode = givenNode
            expected
        }

        // When
        val actual = InterpreterController(
            parameter = emptyMap(),
            variableInterpreter = ParameterizedInterpreterPluginStub(),
            textInterpreter = nestedInterpreter,
            functionSelector = ParameterizedInterpreterPluginStub(),
            compoundInterpreter = ParameterizedInterpreterPluginStub(),
            linkInterpreter = ParameterizedInterpreterPluginStub(),
            freeLinkInterpreter = ParameterizedInterpreterPluginStub()
        ).interpret(node)

        // Then
        actual mustBe expected
        capturedNode!! mustBe node
    }

    @Test
    fun `Given interpret is called with a VariableNode it delegates it to the VariableInterpreter`() {
        // Given
        val parameter: Map<String, String> = fixture()
        val nestedInterpreter = ParameterizedInterpreterPluginStub<CoreNode.VariableNode, Map<String, String>>()
        val expected: String = fixture()
        val node = CoreNode.VariableNode(fixture())
        var capturedNode: BananaContract.Node? = null
        var capturedParameter: Map<String, String> = emptyMap()

        nestedInterpreter.interpret = { givenNode, givenParameter ->
            capturedParameter = givenParameter
            capturedNode = givenNode
            expected
        }

        // When
        val actual = InterpreterController(
            parameter = parameter,
            variableInterpreter = nestedInterpreter,
            textInterpreter = InterpreterPluginStub(),
            functionSelector = ParameterizedInterpreterPluginStub(),
            compoundInterpreter = ParameterizedInterpreterPluginStub(),
            linkInterpreter = ParameterizedInterpreterPluginStub(),
            freeLinkInterpreter = ParameterizedInterpreterPluginStub()
        ).interpret(node)

        // Then
        actual mustBe expected
        capturedNode!! mustBe node
        capturedParameter mustBe parameter
    }

    @Test
    fun `Given interpret is called with a FunctionNode it delegates it to the FunctionSelector`() {
        // Given
        val nestedInterpreter =
            ParameterizedInterpreterPluginStub<CoreNode.FunctionNode, BananaContract.InterpreterController>()
        val controller = InterpreterController(
            parameter = emptyMap(),
            variableInterpreter = ParameterizedInterpreterPluginStub(),
            textInterpreter = InterpreterPluginStub(),
            functionSelector = nestedInterpreter,
            compoundInterpreter = ParameterizedInterpreterPluginStub(),
            linkInterpreter = ParameterizedInterpreterPluginStub(),
            freeLinkInterpreter = ParameterizedInterpreterPluginStub()
        )

        val expected: String = fixture()
        val node = CoreNode.FunctionNode(fixture())
        var capturedNode: BananaContract.Node? = null
        var capturedController: BananaContract.InterpreterController? = null

        nestedInterpreter.interpret = { givenNode, givenParameter ->
            capturedController = givenParameter
            capturedNode = givenNode
            expected
        }

        // When
        val actual = controller.interpret(node)

        // Then
        actual mustBe expected
        capturedNode!! mustBe node
        capturedController!! mustBe controller
    }

    @Test
    fun `Given interpret is called with a CompoundNode it delegates it to the CompoundInterpreter`() {
        // Given
        val nestedInterpreter =
            ParameterizedInterpreterPluginStub<CoreNode.CompoundNode, BananaContract.InterpreterController>()
        val controller = InterpreterController(
            parameter = emptyMap(),
            variableInterpreter = ParameterizedInterpreterPluginStub(),
            textInterpreter = InterpreterPluginStub(),
            functionSelector = ParameterizedInterpreterPluginStub(),
            compoundInterpreter = nestedInterpreter,
            linkInterpreter = ParameterizedInterpreterPluginStub(),
            freeLinkInterpreter = ParameterizedInterpreterPluginStub()
        )

        val expected: String = fixture()
        val node = CoreNode.CompoundNode(emptyList())
        var capturedNode: BananaContract.Node? = null
        var capturedController: BananaContract.InterpreterController? = null

        nestedInterpreter.interpret = { givenNode, givenParameter ->
            capturedController = givenParameter
            capturedNode = givenNode
            expected
        }

        // When
        val actual = controller.interpret(node)

        // Then
        actual mustBe expected
        capturedNode!! mustBe node
        capturedController!! mustBe controller
    }

    @Test
    fun `Given interpret is called with a LinkNode it delegates it to the LinkInterpreter`() {
        // Given
        val nestedInterpreter = ParameterizedInterpreterPluginStub<CoreNode.LinkNode, BananaContract.InterpreterController>()
        val controller = InterpreterController(
            parameter = emptyMap(),
            variableInterpreter = ParameterizedInterpreterPluginStub(),
            textInterpreter = InterpreterPluginStub(),
            functionSelector = ParameterizedInterpreterPluginStub(),
            compoundInterpreter = ParameterizedInterpreterPluginStub(),
            linkInterpreter = nestedInterpreter,
            freeLinkInterpreter = ParameterizedInterpreterPluginStub()
        )

        val expected: String = fixture()
        val node = CoreNode.LinkNode(emptyList())
        var capturedNode: BananaContract.Node? = null
        var capturedController: BananaContract.InterpreterController? = null

        nestedInterpreter.interpret = { givenNode, givenParameter ->
            capturedController = givenParameter
            capturedNode = givenNode
            expected
        }

        // When
        val actual = controller.interpret(node)

        // Then
        actual mustBe expected
        capturedNode!! mustBe node
        capturedController!! mustBe controller
    }

    @Test
    fun `Given interpret is called with a FreeLinkNode it delegates it to the FreeLinkInterpreter`() {
        // Given
        val nestedInterpreter = ParameterizedInterpreterPluginStub<CoreNode.FreeLinkNode, BananaContract.InterpreterController>()
        val controller = InterpreterController(
            parameter = emptyMap(),
            variableInterpreter = ParameterizedInterpreterPluginStub(),
            textInterpreter = InterpreterPluginStub(),
            functionSelector = ParameterizedInterpreterPluginStub(),
            compoundInterpreter = ParameterizedInterpreterPluginStub(),
            linkInterpreter = ParameterizedInterpreterPluginStub(),
            freeLinkInterpreter = nestedInterpreter
        )

        val expected: String = fixture()
        val node = CoreNode.FreeLinkNode(CoreNode.TextNode(emptyList()))
        var capturedNode: BananaContract.Node? = null
        var capturedController: BananaContract.InterpreterController? = null

        nestedInterpreter.interpret = { givenNode, givenParameter ->
            capturedController = givenParameter
            capturedNode = givenNode
            expected
        }

        // When
        val actual = controller.interpret(node)

        // Then
        actual mustBe expected
        capturedNode!! mustBe node
        capturedController!! mustBe controller
    }
}

private class UnknownNode : BananaContract.Node
