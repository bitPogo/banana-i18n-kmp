/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.createLocale
import tech.antibytes.mock.interpreter.InterpreterFactoryStub
import tech.antibytes.mock.interpreter.LinkFormatterStub
import tech.antibytes.mock.interpreter.TextInterceptorSpy
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test
import kotlin.test.assertFailsWith

@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class BananaBuilderSpec {
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

    @Test
    fun `Given setLinkFormatter with a LinkFormatter is called, it returns the Builder`() {
        // Given
        val formatter = LinkFormatterStub()
        val builder = BananaBuilder()

        // When
        val actual = builder.setLinkFormatter(formatter)

        // Then
        actual sameAs builder
    }

    @Test
    fun `Given setLogger with a Logger is called, it returns the Builder`() {
        // Given
        val logger = LoggerStub()
        val builder = BananaBuilder()

        // When
        val actual = builder.setLogger(logger)

        // Then
        actual sameAs builder
    }

    @Test
    fun `Given registerPlugin with a Plugin is called, it returns the Builder`() {
        // Given
        val plugin = PublicApi.Plugin(
            name = "test",
            interpreter = InterpreterFactoryStub()
        )
        val builder = BananaBuilder()

        // When
        val actual = builder.registerPlugin(plugin)

        // Then
        actual sameAs builder
    }

    @Test
    fun `Given build is called, it fails if no Language was set`() {
        // Given
        val builder = BananaBuilder()

        // Then
        val error = assertFailsWith<BananaRuntimeError> {
            // When
            builder.build()
        }

        error.message mustBe "You forgot to set a Language!"
    }

    @Test
    fun `Given build is called and the Language is set, it returns a Instance of BananaI18n`() {
        // Given
        val locale = createLocale("de-DE")
        val builder = BananaBuilder()

        // When
        val actual = builder
            .setLanguage(locale)
            .build()

        // Then
        actual fulfils PublicApi.BananaI18n::class
    }
}
