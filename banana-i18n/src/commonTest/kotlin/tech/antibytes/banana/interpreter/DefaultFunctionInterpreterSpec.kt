/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import kotlin.test.AfterTest
import kotlin.test.Test
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode.CompoundNode
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.banana.ast.CoreNode.TextNode
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.listFixture
import tech.antibytes.mock.LoggerStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

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
        val functionName: String = fixture.fixture()
        val node = FunctionNode(functionName)

        // When
        val result = DefaultFunctionInterpreter(logger).interpret(node)

        // Then
        result mustBe "{{$functionName}}"
        logger.error[0] mustBe Pair(
            PublicApi.Tag.INTERPRETER,
            "Error: Unknown function $functionName in use.",
        )
    }

    @Test
    fun `Given interpret is called with a FunctionNode it swallows Function Arguments`() {
        // Given
        val functionName: String = fixture.fixture()
        val node = FunctionNode(
            functionName,
            CompoundNode(
                listOf(
                    TextNode(
                        fixture.listFixture(),
                    ),
                ),
            ),
        )

        // When
        val result = DefaultFunctionInterpreter(logger).interpret(node)

        // Then
        result mustBe "{{$functionName}}"
    }
}
