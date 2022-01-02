/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import org.junit.runner.RunWith
import tech.antibytes.mock.createLocale
import tech.antibytes.mock.interpreter.TextInterceptorSpy
import tech.antibytes.util.test.annotations.RobolectricConfig
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

@RunWith(RobolectricTestRunner::class)
class BananaBuilderASpec {
    @Test
    fun `It fulfils BananaBuilder`() {
        BananaBuilder() fulfils PublicApi.BananaBuilder::class
    }

    @Test
    fun `Given setLanguage with a Locale is called, it returns the Builder`() {
        // Given
        val locale = createLocale("de-DE")
        val builder = BananaBuilder()

        // When
        val actual = builder.setLanguage(locale)

        // Then
        actual sameAs builder
    }

    @Test
    fun `Given setTextInterceptor with a TextInterceptor is called, it returns the Builder`() {
        // Given
        val interceptor = TextInterceptorSpy()
        val builder = BananaBuilder()

        // When
        val actual = builder.setTextInterceptor(interceptor)

        // Then
        actual sameAs builder
    }
}
