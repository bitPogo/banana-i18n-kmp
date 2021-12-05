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

class CharacterSpec {
    @Test
    fun `It fulfils Character`() {
        Character fulfils TokenizerContract.Character::class
    }

    @Test
    fun `Given codePointAt is called with a CharArray a, start index and a limit, it fails if the index is out of bounce`() {
        // Given
        val array = CharArray(0)

        // Then
        assertFailsWith<Exception> {
            // When
            Character.codePointAt(array, 0, 0)
        }
    }

    @Test
    fun `Given codePointAt is called with a CharArray a, start index and a limit, it fails if the limit is out of bounce`() {
        // Given
        val array = CharArray(1)

        // Then
        assertFailsWith<Exception> {
            // When
            Character.codePointAt(array, 0, 0)
        }
    }

    @Test
    fun `Given codePointAt is called with a CharArray a, start index and a limit, returns the CodePoint`() {
        // Given
        val array = "好".toCharArray()
        // When
        val codePoint = Character.codePointAt(array, 0, 1)

        // Then
        codePoint mustBe 22909
    }
}
