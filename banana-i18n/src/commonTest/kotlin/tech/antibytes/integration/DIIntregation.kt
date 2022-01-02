/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.integration

import org.koin.core.parameter.parametersOf
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.initKoin
import tech.antibytes.mock.interpreter.CustomFunctionInterpreterStub
import tech.antibytes.mock.interpreter.LinkFormatterStub
import tech.antibytes.mock.interpreter.TextInterceptorSpy
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.mock.parser.NodeFactoryStub
import tech.antibytes.mock.parser.ParserPluginFactoryStub
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class DIIntregation {
    private val logger = LoggerStub()
    private val parserPlugins = mapOf(
        "name" to Pair(ParserPluginFactoryStub(), NodeFactoryStub())
    )
    private val textInterceptor = TextInterceptorSpy()
    private val linkFormatter = LinkFormatterStub()
    private val interpreterPlugins = mapOf(
        "name" to CustomFunctionInterpreterStub()
    )
    private val variables = mapOf("1" to "1")
    private val message = "simple message string"

    @Test
    fun `Given initKoin is called with its parameter it resolves the TokenStore`() {
        // When
        val koin = initKoin(
            logger,
            parserPlugins,
            textInterceptor,
            linkFormatter,
            interpreterPlugins
        )

        val actual: PublicApi.TokenStore = koin.koin.get(
            parameters = { parametersOf(message) }
        )

        // Then
        actual fulfils PublicApi.TokenStore::class
    }

    @Test
    fun `Given initKoin is called with its parameter it resolves the TopLevelParser`() {
        // When
        val koin = initKoin(
            logger,
            parserPlugins,
            textInterceptor,
            linkFormatter,
            interpreterPlugins
        )

        val actual: BananaContract.TopLevelParser = koin.koin.get()

        // Then
        actual fulfils BananaContract.TopLevelParser::class
    }

    @Test
    fun `Given initKoin is called with its parameter it resolves the Interpreter`() {
        // When
        val koin = initKoin(
            logger,
            parserPlugins,
            textInterceptor,
            linkFormatter,
            interpreterPlugins
        )

        val actual: PublicApi.InterpreterController = koin.koin.get(
            parameters = {
                parametersOf(variables)
            }
        )

        // Then
        actual fulfils PublicApi.InterpreterController::class
    }
}
