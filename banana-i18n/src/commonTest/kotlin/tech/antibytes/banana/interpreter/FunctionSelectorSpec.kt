/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.InternalNodes.FunctionNode
import tech.antibytes.mock.interpreter.FunctionInterpreterStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

class FunctionSelectorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils InterpreterPlugin`() {
        FunctionSelector(FunctionInterpreterStub(), emptyMap()) fulfils BananaContract.InterpreterPlugin::class
    }

    @Test
    fun `Given interpret is called with a FunctionNode it uses the given DefaultInterpreter if the name does not match`() {
        // Given
        val captured = mutableListOf<FunctionNode>()
        val default = FunctionInterpreterStub { node ->
            node.id.also { captured.add(node) }
        }
        val functionName: String = fixture()
        val node = FunctionNode(functionName)

        // When
        FunctionSelector(default, emptyMap()).interpret(node)

        // Then
        captured.isEmpty() mustBe false
        captured[0] sameAs node
    }

    @Test
    fun `Given interpret is called with a FunctionNode it uses a given Plugin if the name matches`() {
        // Given
        val captured = mutableListOf<FunctionNode>()
        val plugin = FunctionInterpreterStub { node ->
            node.id.also { captured.add(node) }
        }
        val functionName: String = fixture()
        val node = FunctionNode(functionName)

        val plugins = mapOf(
            functionName to plugin
        )

        // When
        FunctionSelector(
            FunctionInterpreterStub(),
            plugins
        ).interpret(node)

        // Then
        captured.isEmpty() mustBe false
        captured[0] sameAs node
    }
}
