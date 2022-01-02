/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.createLocale
import tech.antibytes.mock.interpreter.CustomFunctionInterpreterStub
import tech.antibytes.mock.interpreter.InterpreterFactoryStub
import tech.antibytes.mock.interpreter.LinkFormatterStub
import tech.antibytes.mock.interpreter.TextInterceptorSpy
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class BananaBuilderSpec {
    private val locale = createLocale("de-DE")

    @Test
    fun `It fulfils BananaBuilder`() {
        BananaBuilder(locale) fulfils PublicApi.BananaBuilder::class
    }

    @Test
    fun `Given setLanguage with a Locale is called, it returns the Builder`() {
        // Given
        val locale = createLocale("en-DE")
        val builder = BananaBuilder(locale)

        // When
        val actual = builder.setLanguage(locale)

        // Then
        actual sameAs builder
    }

    @Test
    fun `Given setTextInterceptor with a TextInterceptor is called, it returns the Builder`() {
        // Given
        val interceptor = TextInterceptorSpy()
        val builder = BananaBuilder(locale)

        // When
        val actual = builder.setTextInterceptor(interceptor)

        // Then
        actual sameAs builder
    }

    @Test
    fun `Given setLinkFormatter with a LinkFormatter is called, it returns the Builder`() {
        // Given
        val formatter = LinkFormatterStub()
        val builder = BananaBuilder(locale)

        // When
        val actual = builder.setLinkFormatter(formatter)

        // Then
        actual sameAs builder
    }

    @Test
    fun `Given setLogger with a Logger is called, it returns the Builder`() {
        // Given
        val logger = LoggerStub()
        val builder = BananaBuilder(locale)

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
        val builder = BananaBuilder(locale)

        // When
        val actual = builder.registerPlugin(plugin)

        // Then
        actual sameAs builder
    }

    @Test
    fun `Given build is called and the Language is set, it returns a Instance of BananaI18n`() {
        // Given
        val builder = BananaBuilder(locale)

        // When
        val actual = builder
            .build()

        // Then
        actual fulfils PublicApi.BananaI18n::class
    }

    @Test
    fun `Given build is called it initalizes given InterpreterPlugins`() {
        // Given
        val factory = InterpreterFactoryStub()
        val logger = LoggerStub()
        val plugin = PublicApi.Plugin(
            name = "test",
            interpreter = factory
        )

        val builder = BananaBuilder(locale)

        var capturedLocale: Locale? = null
        var capturedLogger: PublicApi.Logger? = null

        factory.getInstance = { givenLogger, givenLocale ->
            capturedLogger = givenLogger
            capturedLocale = givenLocale

            CustomFunctionInterpreterStub()
        }

        // When
        val actual = builder
            .setLogger(logger)
            .registerPlugin(plugin)
            .build()

        // Then
        actual fulfils PublicApi.BananaI18n::class
        capturedLocale!! mustBe locale
        capturedLogger!! mustBe logger
    }

    @Test
    fun `Given build is called it initalizes given InterpreterPlugins with resetted Locale`() {
        // Given
        val resettedLocale = createLocale("en-DE")
        val factory = InterpreterFactoryStub()
        val logger = LoggerStub()
        val plugin = PublicApi.Plugin(
            name = "test",
            interpreter = factory
        )

        val builder = BananaBuilder(locale)

        var capturedLocale: Locale? = null
        var capturedLogger: PublicApi.Logger? = null

        factory.getInstance = { givenLogger, givenLocale ->
            capturedLogger = givenLogger
            capturedLocale = givenLocale

            CustomFunctionInterpreterStub()
        }

        // When
        val actual = builder
            .setLogger(logger)
            .setLanguage(resettedLocale)
            .registerPlugin(plugin)
            .build()

        // Then
        actual fulfils PublicApi.BananaI18n::class
        capturedLocale!! mustBe resettedLocale
        capturedLogger!! mustBe logger
    }
}
