/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test
import kotlin.test.assertFailsWith

class StringReaderSpec {
    @Test
    fun `It fulfils Reader`() {
        StringReader("test") fulfils TokenizerContract.Reader::class
    }

    @Test
    fun `Given close is called, it just runs`() {
        // Given
        val reader = StringReader("test")

        // When
        val result = reader.close()
        reader.close()

        // Then
        result mustBe Unit
    }

    @Test
    fun `Given read is called it fails, if the reader was already closed`() {
        // Given
        val reader = StringReader("test")

        // When
        reader.close()

        assertFailsWith<Exception> {
            // Then
            reader.read()
        }
    }

    @Test
    fun `Given read is called it returns a Char in its numeric representation`() {
        // Given
        val value = "test"
        val reader = StringReader("test")

        // When
        val result = reader.read()

        // Then
        result mustBe value[0].code
    }

    @Test
    fun `Given read is called with a Buffer, an Offset and a Limit, it fails if the reader was already closed`() {
        // Given
        val reader = StringReader("test")
        val buffer = CharArray(23)

        // When
        reader.close()

        assertFailsWith<Exception> {
            // Then
            reader.read(buffer, 0, 3)
        }
    }

    @Test
    fun `Given read is called with a Buffer, an Offset and a Limit, it fails if the Offeset or Limit is out of Bounce`() {
        // Given
        val reader = StringReader("test")
        val buffer = CharArray(23)

        // When
        assertFailsWith<Exception> {
            // Then
            reader.read(buffer, -1, 3)
        }

        assertFailsWith<Exception> {
            // Then
            reader.read(buffer, 0, 24)
        }

        assertFailsWith<Exception> {
            // Then
            reader.read(buffer, 24, 3)
        }
    }

    @Test
    fun `Given read is called with a Buffer, an Offset and a Limit, it writes into the buffer for the given Offset and Limit`() {
        // Given
        val reader = StringReader("Test")
        val buffer = CharArray(23)
        for (idx in 0..buffer.lastIndex) {
            buffer[idx] = ' '
        }

        buffer[0] = 'x'

        // When
        val result = reader.read(buffer, 1, 4)

        // Then
        result mustBe 4
        String(buffer).trim() mustBe "xTest"
    }
}
