/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CompoundNode
import tech.antibytes.banana.ast.FunctionNode
import tech.antibytes.banana.ast.TextNode
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class DefaultFunctionInterpreterSpec {
    private val fixture = kotlinFixture()
    private val logger = LoggerStub()

    @AfterTest
    fun tearDown() {
        logger.clear()
    }

    @Test
    fun `It fulfils InterpreterPlugin`() {
        DefaultFunctionInterpreter(logger) fulfils BananaContract.InterpreterPlugin::class
    }

    @Test
    fun `Given interpret is called with a FunctionNode it returns the Function name in FunctionForm and logs an error`() {
        // Given
        val functionName: String = fixture()
        val node = FunctionNode(functionName)

        // When
        val result = DefaultFunctionInterpreter(logger).interpret(node)

        // Then
        result mustBe "{{$functionName}}"
        logger.error[0] mustBe Pair(
            BananaContract.Tag.INTERPRETER,
            "Error: Unknown function $functionName in use."
        )
    }

    @Test
    fun `Given interpret is called with a FunctionNode it swallows Function Arguments`() {
        // Given
        val functionName: String = fixture()
        val node = FunctionNode(
            functionName,
            CompoundNode(listOf(TextNode(listOf(fixture()))))
        )

        // When
        val result = DefaultFunctionInterpreter(logger).interpret(node)

        // Then
        result mustBe "{{$functionName}}"
    }
}
