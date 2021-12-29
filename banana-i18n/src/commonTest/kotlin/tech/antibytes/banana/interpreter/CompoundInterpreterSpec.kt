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

class CompoundInterpreterSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils ParameterizedInterpreterPlugin`() {
        CompoundInterpreter() fulfils BananaContract.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given interpret is called with a CompoundNode and a InterpreterController, it delegates the Children to the Controller`() {
        // Given
        val controller = InterpreterControllerStub()
        val capturedNodes: MutableList<BananaContract.Node> = mutableListOf()

        val node = CoreNodes.CompoundNode(
            listOf(
                CoreNodes.TextNode(fixture()),
                CoreNodes.VariableNode(fixture()),
                CoreNodes.FunctionNode(fixture())
            )
        )

        controller.interpret = { givenNode ->
            capturedNodes.add(givenNode)
            fixture()
        }

        // When
        CompoundInterpreter().interpret(node, controller)

        // Then
        capturedNodes mustBe node.children
    }

    @Test
    fun `Given interpret is called with a CompoundNode and a InterpreterController, it concartinates the Controllers Output`() {
        // Given
        val controller = InterpreterControllerStub()
        val output: MutableList<String> = mutableListOf(
            fixture(),
            fixture(),
            fixture()
        )

        val expected = output.joinToString("")

        val node = CoreNodes.CompoundNode(
            listOf(
                CoreNodes.TextNode(fixture()),
                CoreNodes.VariableNode(fixture()),
                CoreNodes.FunctionNode(fixture())
            )
        )

        controller.interpret = { output.removeFirst() }

        // When
        val actual = CompoundInterpreter().interpret(node, controller)

        // Then
        actual mustBe expected
    }
}
