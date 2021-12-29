/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.InternalNodes
import tech.antibytes.mock.interpreter.InterpreterPluginStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class InterpreterControllerSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils InterpreterController`() {
        InterpreterController(
            InterpreterPluginStub(),
        ) fulfils BananaContract.InterpreterController::class
    }

    @Test
    fun `Given interpret is called with a TextNode it delegates it to the TextInterpreter`() {
        // Given
        val nestedInterpreter = InterpreterPluginStub<InternalNodes.TextNode>()
        val expected: String = fixture()
        val givenNode = InternalNodes.TextNode(fixture())
        var capturedNode: BananaContract.Node? = null

        nestedInterpreter.interpret = { node ->
            capturedNode = node
            expected
        }

        // When
        val actual = InterpreterController(
            textInterpreter = nestedInterpreter,
        ).interpret(givenNode)

        // Then
        actual mustBe expected
        capturedNode!! mustBe givenNode
    }
}
