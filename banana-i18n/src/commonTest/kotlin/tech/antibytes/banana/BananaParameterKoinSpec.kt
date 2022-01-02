/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.interpreter.CustomFunctionInterpreterStub
import tech.antibytes.mock.interpreter.LinkFormatterStub
import tech.antibytes.mock.interpreter.TextInterceptorSpy
import tech.antibytes.mock.parser.NodeFactoryStub
import tech.antibytes.mock.parser.ParserPluginFactoryStub
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

class BananaParameterKoinSpec {
    @Test
    fun `Given resolveBananaParameterModule is called wit a Logger, the resulting Module holds it`() {
        // Given
        val logger = LoggerStub()
        val koin = koinApplication {
            modules(
                resolveBananaParameterModule(
                    logger,
                    emptyMap(),
                    TextInterceptorSpy(),
                    LinkFormatterStub(),
                    emptyMap(),
                )
            )
        }

        // When
        val actual = koin.koin.get<PublicApi.Logger>()

        // Then
        actual sameAs logger
    }

    @Test
    fun `Given resolveBananaParameterModule is called wit a ParserPlugins, the resulting Module holds it`() {
        // Given
        val pluginId = "name"
        val customPlugin = Pair(ParserPluginFactoryStub(), NodeFactoryStub())
        val plugins = mapOf(
            pluginId to customPlugin
        )

        val koin = koinApplication {
            modules(
                resolveBananaParameterModule(
                    LoggerStub(),
                    plugins,
                    TextInterceptorSpy(),
                    LinkFormatterStub(),
                    emptyMap(),
                )
            )
        }

        // When
        val actual = koin.koin.get<ParserPluginMap>(named(BananaContract.KoinLabels.PARSER_PLUGINS))

        // Then
        actual sameAs plugins
    }

    @Test
    fun `Given resolveBananaParameterModule is called wit a TextInterceptor, the resulting Module holds it`() {
        // Given
        val interceptor = TextInterceptorSpy()
        val koin = koinApplication {
            modules(
                resolveBananaParameterModule(
                    LoggerStub(),
                    emptyMap(),
                    interceptor,
                    LinkFormatterStub(),
                    emptyMap(),
                )
            )
        }

        // When
        val actual = koin.koin.get<PublicApi.TextInterceptor>()

        // Then
        actual sameAs interceptor
    }

    @Test
    fun `Given resolveBananaParameterModule is called wit a LinkFormatter, the resulting Module holds it`() {
        // Given
        val formatter = LinkFormatterStub()
        val koin = koinApplication {
            modules(
                resolveBananaParameterModule(
                    LoggerStub(),
                    emptyMap(),
                    TextInterceptorSpy(),
                    formatter,
                    emptyMap(),
                )
            )
        }

        // When
        val actual = koin.koin.get<PublicApi.LinkFormatter>()

        // Then
        actual sameAs formatter
    }

    @Test
    fun `Given resolveBananaParameterModule is called wit InterpreterPlugins, the resulting Module holds it`() {
        // Given
        val plugin = CustomFunctionInterpreterStub()
        val functionName = "name"
        val plugins = mapOf(
            functionName to plugin
        )

        val koin = koinApplication {
            modules(
                resolveBananaParameterModule(
                    LoggerStub(),
                    emptyMap(),
                    TextInterceptorSpy(),
                    LinkFormatterStub(),
                    plugins
                )
            )
        }

        // When
        val actual = koin.koin.get<RegisteredInterpreterPlugins>(named(BananaContract.KoinLabels.INTERPRETER_PLUGINS))

        // Then
        actual sameAs plugins
    }
}
