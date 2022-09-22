/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import kotlin.test.Test
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.mock.interpreter.InterpreterControllerStub
import tech.antibytes.mock.interpreter.NodeConcatenatorStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class CompoundInterpreterSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils ParameterizedInterpreterPlugin`() {
        CompoundInterpreter(NodeConcatenatorStub()) fulfils PublicApi.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given interpret is called with a CompoundNode and a InterpreterController, it delegates the Children and the Controller to Concatrinator and returns its output`() {
        // Given
        val controller = InterpreterControllerStub()
        val concatenator = NodeConcatenatorStub()

        val node = CoreNode.CompoundNode(
            listOf(
                CoreNode.TextNode(fixture.listFixture()),
                CoreNode.VariableNode(fixture.fixture()),
                CoreNode.FunctionNode(fixture.fixture()),
            ),
        )

        val expected: String = fixture.fixture()
        var capturedNodes: List<PublicApi.Node> = emptyList()
        var capturedController: PublicApi.InterpreterController? = null

        concatenator.concatenate = { givenNodes, givenController ->
            capturedNodes = givenNodes
            capturedController = givenController
            expected
        }

        // When
        val actual = CompoundInterpreter(concatenator).interpret(node, controller)

        // Then
        capturedNodes mustBe node.children
        capturedController!! mustBe controller
        actual mustBe expected
    }
}
