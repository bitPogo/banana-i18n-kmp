/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import kotlin.test.AfterTest
import kotlin.test.Test
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNode.TextNode
import tech.antibytes.mock.interpreter.TextInterceptorSpy
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class TextInterpreterSpec {
    private val interceptor = TextInterceptorSpy()

    @AfterTest
    fun tearDown() {
        interceptor.clear()
    }

    @Test
    fun `It fulfils InterpreterPlugin`() {
        TextInterpreter(interceptor) fulfils BananaContract.InterpreterPlugin::class
    }

    @Test
    fun `Given interpret is called with a TextNode it returns the Nodes value concartinated`() {
        // Given
        val chunks = listOf("A", " ", "B", " ", "C")

        // When
        val result = TextInterpreter(interceptor).interpret(TextNode(chunks))

        // Then
        result mustBe chunks.joinToString("")
    }

    @Test
    fun `Given interpret is called with a TextNode it delegates the Nodes value to the TextInterceptor`() {
        // Given
        val chunks = listOf("A", " ", "B", " ", "C")
        val mapping = mapOf(
            "A" to "D",
            "B" to "E",
            "C" to "F",
        )
        interceptor.intercept = { chunk -> mapping.getOrElse(chunk) { " " } }

        // When
        val result = TextInterpreter(interceptor).interpret(TextNode(chunks))

        // Then
        result mustBe mapping.values.joinToString(" ")
    }
}
