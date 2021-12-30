/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.mock.interpreter.CustomFunctionInterpreterStub
import tech.antibytes.mock.interpreter.FunctionInterpreterStub
import tech.antibytes.mock.interpreter.InterpreterControllerStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

class FunctionSelectorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils ParameterizedInterpreterPlugin`() {
        FunctionSelector(
            FunctionInterpreterStub(),
            emptyMap()
        ) fulfils BananaContract.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given interpret is called with a FunctionNode it uses the given DefaultInterpreter if the name does not match`() {
        // Given
        var capturedNode: FunctionNode? = null
        val default = FunctionInterpreterStub { givenNode ->
            givenNode.id.also { capturedNode = givenNode }
        }
        val functionName: String = fixture()
        val node = FunctionNode(functionName)

        // When
        val actual = FunctionSelector(default, emptyMap()).interpret(node, InterpreterControllerStub())

        // Then
        actual mustBe node.id
        capturedNode!! sameAs node
    }

    @Test
    fun `Given interpret is called with a FunctionNode it uses a given Plugin if the name matches`() {
        // Given
        var capturedNode: BananaContract.Node? = null
        var capturedController: BananaContract.InterpreterController? = null

        val controller = InterpreterControllerStub()
        val plugin = CustomFunctionInterpreterStub { givenNode, givenController ->
            capturedNode = givenNode
            capturedController = givenController
            givenNode.id
        }
        val functionName: String = fixture()
        val node = FunctionNode(functionName)

        val plugins = mapOf(
            functionName to plugin
        )

        // When
        val actual = FunctionSelector(
            FunctionInterpreterStub(),
            plugins
        ).interpret(node, controller)

        // Then
        actual mustBe node.id
        capturedNode!! sameAs node
        capturedController!! sameAs controller
    }
}
