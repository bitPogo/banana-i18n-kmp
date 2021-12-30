/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.mock.interpreter.InterpreterControllerStub
import tech.antibytes.mock.interpreter.NodeConcatenatorStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class CompoundInterpreterSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils ParameterizedInterpreterPlugin`() {
        CompoundInterpreter(NodeConcatenatorStub()) fulfils BananaContract.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given interpret is called with a CompoundNode and a InterpreterController, it delegates the Children and the Controller to Concatrinator and returns its output`() {
        // Given
        val controller = InterpreterControllerStub()
        val concatenator = NodeConcatenatorStub()

        val node = CoreNode.CompoundNode(
            listOf(
                CoreNode.TextNode(fixture()),
                CoreNode.VariableNode(fixture()),
                CoreNode.FunctionNode(fixture())
            )
        )

        val expected: String = fixture()
        var capturedNodes: List<BananaContract.Node> = emptyList()
        var capturedController: BananaContract.InterpreterController? = null

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
