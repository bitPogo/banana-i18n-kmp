/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNodes.VariableNode
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class VariableInterpreterSpec {
    private val fixture = kotlinFixture()
    private val logger = LoggerStub()

    @AfterTest
    fun tearDown() {
        logger.clear()
    }

    @Test
    fun `It fulfils ParameterizedInterpreterPlugin`() {
        VariableInterpreter(logger) fulfils BananaContract.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given interprete is called with a VariableNode and a Map, it returns the value of a Entry`() {
        // Given
        val id = "abc"
        val value: String = fixture()
        val parameter = mapOf(id to value)

        val node = VariableNode(id)

        // When
        val result = VariableInterpreter(logger).interpret(node, parameter)

        // Then
        result mustBe value
        logger.error.isEmpty() mustBe true
    }

    @Test
    fun `Given interprete is called with a VariableNode and a Map, it logs an Error and returns the ID of the not matched Entry`() {
        // Given
        val id: String = fixture()
        val value: String = fixture()
        val parameter = mapOf("any" to value)

        val node = VariableNode(id)

        // When
        val result = VariableInterpreter(logger).interpret(node, parameter)

        // Then
        result mustBe "\$${id}"
        logger.error.isEmpty() mustBe false
        logger.error[0] mustBe Pair(
            BananaContract.Tag.INTERPRETER,
            "Error: Unknown variable ($id)."
        )
    }
}
