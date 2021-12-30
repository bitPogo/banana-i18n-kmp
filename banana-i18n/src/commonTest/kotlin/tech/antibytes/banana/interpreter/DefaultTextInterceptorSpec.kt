/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class DefaultTextInterceptorSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils TextInterceptor`() {
        DefaultTextInterceptor() fulfils PublicApi.TextInterceptor::class
    }

    @Test
    fun `Given intercept is called with a String it reflects it`() {
        // Given
        val chunk: String = fixture()

        // When
        val result = DefaultTextInterceptor().intercept(chunk)

        // Then
        result mustBe chunk
    }
}
