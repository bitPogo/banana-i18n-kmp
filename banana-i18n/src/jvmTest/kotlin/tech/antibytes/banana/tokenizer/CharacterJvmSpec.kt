/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import kotlin.test.Test
import tech.antibytes.util.test.mustBe

class CharacterJvmSpec {
    @Test
    fun `Given charCount is called with a char it returns 1, if one Character is sufficient`() {
        // Given
        val char = 0x010000 - 1

        // When
        val amount = Character.charCount(char)

        // Then
        amount mustBe 1
    }

    @Test
    fun `Given charCount is called with a char it returns 2, if one Character is sufficient`() {
        // Given
        val char = 0x010000

        // When
        val amount = Character.charCount(char)

        // Then
        amount mustBe 2
    }
}
