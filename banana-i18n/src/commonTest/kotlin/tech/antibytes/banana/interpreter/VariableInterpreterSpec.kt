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
import tech.antibytes.banana.ast.CoreNode.VariableNode
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.mock.LoggerStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class VariableInterpreterSpec {
    private val fixture = kotlinFixture()
    private val logger = LoggerStub()

    @AfterTest
    fun tearDown() {
        logger.clear()
    }

    @Test
    fun `It fulfils variablesizedInterpreterPlugin`() {
        VariableInterpreter(logger) fulfils BananaContract.VariableInterpreter::class
    }

    @Test
    fun `Given interprete is called with a VariableNode and a Map, it returns the value of a Entry`() {
        // Given
        val id = "abc"
        val value: String = fixture.fixture()
        val variables = mapOf(id to value)

        val node = VariableNode(id)

        // When
        val result = VariableInterpreter(logger).interpret(node, variables)

        // Then
        result mustBe value
        logger.error.isEmpty() mustBe true
    }

    @Test
    fun `Given interprete is called with a VariableNode and a Map, it logs an Error and returns the ID of the not matched Entry`() {
        // Given
        val id: String = fixture.fixture()
        val value: String = fixture.fixture()
        val variables = mapOf("any" to value)

        val node = VariableNode(id)

        // When
        val result = VariableInterpreter(logger).interpret(node, variables)

        // Then
        result mustBe "\$$id"
        logger.error.isEmpty() mustBe false
        logger.error[0] mustBe Pair(
            PublicApi.Tag.INTERPRETER,
            "Error: Unknown variable ($id).",
        )
    }
}
