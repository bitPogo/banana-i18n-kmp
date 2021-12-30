/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class DefaultLinkFormatterSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils LinkFormatter`() {
        DefaultLinkFormatter() fulfils BananaContract.LinkFormatter::class
    }

    @Test
    fun `Given formatLink is called with a target and a empty displayText it returns the target`() {
        // Given
        val target: String = fixture()
        val displayText = ""

        // When
        val actual = DefaultLinkFormatter().formatLink(target, displayText)

        // Then
        actual mustBe target
    }

    @Test
    fun `Given formatLink is called with a target and a displayText it returns the displayText`() {
        // Given
        val target: String = fixture()
        val displayText: String = fixture()

        // When
        val actual = DefaultLinkFormatter().formatLink(target, displayText)

        // Then
        actual mustBe displayText
    }

    @Test
    fun `Given formatFreeLink is called with a URL and a empty displayText it returns the target`() {
        // Given
        val url: String = fixture()
        val displayText = ""

        // When
        val actual = DefaultLinkFormatter().formatLink(url, displayText)

        // Then
        actual mustBe url
    }

    @Test
    fun `Given formatFreeLink is called with a URL and a displayText it returns the displayText`() {
        // Given
        val url: String = fixture()
        val displayText: String = fixture()

        // When
        val actual = DefaultLinkFormatter().formatLink(url, displayText)

        // Then
        actual mustBe displayText
    }
}
