/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class CharacterAndroidSpec {
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
