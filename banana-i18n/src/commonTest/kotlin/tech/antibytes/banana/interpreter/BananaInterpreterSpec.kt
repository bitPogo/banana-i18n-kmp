/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import kotlin.test.Test
import kotlin.test.assertFailsWith
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.Variables
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.kfixture.mapFixture
import tech.antibytes.mock.interpreter.InterpreterPluginStub
import tech.antibytes.mock.interpreter.ParameterizedInterpreterPluginStub
import tech.antibytes.mock.interpreter.VariableInterpreterStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class BananaInterpreterSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils InterpreterController`() {
        BananaInterpreter(
            emptyMap(),
            VariableInterpreterStub(),
            InterpreterPluginStub(),
            ParameterizedInterpreterPluginStub(),
            ParameterizedInterpreterPluginStub(),
            ParameterizedInterpreterPluginStub(),
            ParameterizedInterpreterPluginStub(),
        ) fulfils PublicApi.InterpreterController::class
    }

    @Test
    fun `Given interpret is called with a Unknown Node it fails`() {
        // Given
        val node = UnknownNode()

        // When
        val actual = assertFailsWith<FatalInterpreterError> {
            BananaInterpreter(
                variables = emptyMap(),
                variableInterpreter = VariableInterpreterStub(),
                textInterpreter = InterpreterPluginStub(),
                functionSelector = ParameterizedInterpreterPluginStub(),
                compoundInterpreter = ParameterizedInterpreterPluginStub(),
                linkInterpreter = ParameterizedInterpreterPluginStub(),
                freeLinkInterpreter = ParameterizedInterpreterPluginStub(),
            ).interpret(node)
        }

        // Then
        actual.message mustBe "Unknown Node detected!"
    }

    @Test
    fun `Given interpret is called with a TextNode it delegates it to the TextInterpreter`() {
        // Given
        val nestedInterpreter = InterpreterPluginStub<CoreNode.TextNode>()
        val expected: String = fixture.fixture()
        val node = CoreNode.TextNode(fixture.listFixture())
        var capturedNode: PublicApi.Node? = null

        nestedInterpreter.interpret = { givenNode ->
            capturedNode = givenNode
            expected
        }

        // When
        val actual = BananaInterpreter(
            variables = emptyMap(),
            variableInterpreter = VariableInterpreterStub(),
            textInterpreter = nestedInterpreter,
            functionSelector = ParameterizedInterpreterPluginStub(),
            compoundInterpreter = ParameterizedInterpreterPluginStub(),
            linkInterpreter = ParameterizedInterpreterPluginStub(),
            freeLinkInterpreter = ParameterizedInterpreterPluginStub(),
        ).interpret(node)

        // Then
        actual mustBe expected
        capturedNode!! mustBe node
    }

    @Test
    fun `Given interpret is called with a VariableNode it delegates it to the VariableInterpreter`() {
        // Given
        val variables: Variables = fixture.mapFixture()
        val nestedInterpreter = VariableInterpreterStub()
        val expected: String = fixture.fixture()
        val node = CoreNode.VariableNode(fixture.fixture())
        var capturedNode: PublicApi.Node? = null
        var capturedVariables: Variables = emptyMap()

        nestedInterpreter.interpret = { givenNode, givenVariables ->
            capturedNode = givenNode
            capturedVariables = givenVariables
            expected
        }

        // When
        val actual = BananaInterpreter(
            variables = variables,
            variableInterpreter = nestedInterpreter,
            textInterpreter = InterpreterPluginStub(),
            functionSelector = ParameterizedInterpreterPluginStub(),
            compoundInterpreter = ParameterizedInterpreterPluginStub(),
            linkInterpreter = ParameterizedInterpreterPluginStub(),
            freeLinkInterpreter = ParameterizedInterpreterPluginStub(),
        ).interpret(node)

        // Then
        actual mustBe expected
        capturedNode!! mustBe node
        capturedVariables mustBe variables
    }

    @Test
    fun `Given interpret is called with a FunctionNode it delegates it to the FunctionSelector`() {
        // Given
        val nestedInterpreter = ParameterizedInterpreterPluginStub<CoreNode.FunctionNode>()
        val controller = BananaInterpreter(
            variables = emptyMap(),
            variableInterpreter = VariableInterpreterStub(),
            textInterpreter = InterpreterPluginStub(),
            functionSelector = nestedInterpreter,
            compoundInterpreter = ParameterizedInterpreterPluginStub(),
            linkInterpreter = ParameterizedInterpreterPluginStub(),
            freeLinkInterpreter = ParameterizedInterpreterPluginStub(),
        )

        val expected: String = fixture.fixture()
        val node = CoreNode.FunctionNode(fixture.fixture())
        var capturedNode: PublicApi.Node? = null
        var capturedController: PublicApi.InterpreterController? = null

        nestedInterpreter.interpret = { givenNode, givenController ->
            capturedNode = givenNode
            capturedController = givenController
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
        val nestedInterpreter = ParameterizedInterpreterPluginStub<CoreNode.CompoundNode>()
        val controller = BananaInterpreter(
            variables = emptyMap(),
            variableInterpreter = VariableInterpreterStub(),
            textInterpreter = InterpreterPluginStub(),
            functionSelector = ParameterizedInterpreterPluginStub(),
            compoundInterpreter = nestedInterpreter,
            linkInterpreter = ParameterizedInterpreterPluginStub(),
            freeLinkInterpreter = ParameterizedInterpreterPluginStub(),
        )

        val expected: String = fixture.fixture()
        val node = CoreNode.CompoundNode(emptyList())
        var capturedNode: PublicApi.Node? = null
        var capturedController: PublicApi.InterpreterController? = null

        nestedInterpreter.interpret = { givenNode, givenController ->
            capturedNode = givenNode
            capturedController = givenController
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
        val nestedInterpreter = ParameterizedInterpreterPluginStub<CoreNode.LinkNode>()
        val controller = BananaInterpreter(
            variables = emptyMap(),
            variableInterpreter = VariableInterpreterStub(),
            textInterpreter = InterpreterPluginStub(),
            functionSelector = ParameterizedInterpreterPluginStub(),
            compoundInterpreter = ParameterizedInterpreterPluginStub(),
            linkInterpreter = nestedInterpreter,
            freeLinkInterpreter = ParameterizedInterpreterPluginStub(),
        )

        val expected: String = fixture.fixture()
        val node = CoreNode.LinkNode(emptyList())
        var capturedNode: PublicApi.Node? = null
        var capturedController: PublicApi.InterpreterController? = null

        nestedInterpreter.interpret = { givenNode, givenController ->
            capturedNode = givenNode
            capturedController = givenController
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
        val nestedInterpreter = ParameterizedInterpreterPluginStub<CoreNode.FreeLinkNode>()
        val controller = BananaInterpreter(
            variables = emptyMap(),
            variableInterpreter = VariableInterpreterStub(),
            textInterpreter = InterpreterPluginStub(),
            functionSelector = ParameterizedInterpreterPluginStub(),
            compoundInterpreter = ParameterizedInterpreterPluginStub(),
            linkInterpreter = ParameterizedInterpreterPluginStub(),
            freeLinkInterpreter = nestedInterpreter,
        )

        val expected: String = fixture.fixture()
        val node = CoreNode.FreeLinkNode(CoreNode.TextNode(emptyList()))
        var capturedNode: PublicApi.Node? = null
        var capturedController: PublicApi.InterpreterController? = null

        nestedInterpreter.interpret = { givenNode, givenController ->
            capturedNode = givenNode
            capturedController = givenController
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

private class UnknownNode : PublicApi.Node
