/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class XMLInterceptorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils TextInterceptor`() {
        XMLInterceptor() fulfils PublicApi.TextInterceptor::class
    }

    @Test
    fun `Given intercept is called with a String it reflects it, if is not a XML special character`() {
        // Given
        val chunk: String = fixture.fixture()

        // When
        val result = XMLInterceptor().intercept(chunk)

        // Then
        result mustBe chunk
    }

    @Test
    fun `Given intercept is called with a String it replaces XML speical characters`() {
        // Given
        val interceptor = XMLInterceptor()
        val specials = mapOf(
            "<" to "&lt;",
            ">" to "&gt;",
            "&" to "&amp;",
            "'" to "&#39;",
            "\"" to "&quot;"
        )

        for (special in specials) {
            // When
            val result = interceptor.intercept(special.key)

            // Then
            result mustBe special.value
        }
    }
}
