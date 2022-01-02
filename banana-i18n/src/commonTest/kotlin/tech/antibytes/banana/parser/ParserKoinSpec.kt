/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.mock.parser.NodeFactoryStub
import tech.antibytes.mock.parser.ParserPluginControllerStub
import tech.antibytes.mock.parser.ParserPluginFactoryStub
import tech.antibytes.mock.tokenizer.TokenizerStub
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class ParserKoinSpec {
    @Test
    fun `Given resolveParserModule is called it contains a DefaultArgumentParser Pair`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveParserModule(),
                module {
                    single<PublicApi.NodeFactory>(named(BananaContract.KoinLabels.COMPOUND_FACTORY)) {
                        NodeFactoryStub()
                    }
                }
            )
        }

        // When
        val default: Pair<PublicApi.ParserPluginFactory, PublicApi.NodeFactory> = koin.koin.get(
            named(BananaContract.KoinLabels.DEFAULT_ARGUMENT_PARSER)
        )

        // Then
        default.first fulfils DefaultArgumentsParser.Companion::class
        default.second fulfils NodeFactoryStub::class
    }

    @Test
    fun `Given resolveParserModule is called it contains a ParserPluginController`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveParserModule(),
                module {
                    single<Map<String, Pair<PublicApi.ParserPluginFactory, PublicApi.NodeFactory>>>(named(BananaContract.KoinLabels.PARSER_PLUGINS)) {
                        emptyMap()
                    }

                    single<PublicApi.Logger> {
                        LoggerStub()
                    }

                    single<Pair<PublicApi.ParserPluginFactory, PublicApi.NodeFactory>>(named(BananaContract.KoinLabels.DEFAULT_ARGUMENT_PARSER)) {
                        Pair(
                            ParserPluginFactoryStub(),
                            NodeFactoryStub()
                        )
                    }
                }
            )
        }

        // When
        val controller: PublicApi.ParserPluginController = koin.koin.get()

        // Then
        controller fulfils PublicApi.ParserPluginController::class
    }

    @Test
    fun `Given resolveParserModule is called it contains a BananaParser`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveParserModule(),
                module {
                    single<PublicApi.Logger> {
                        LoggerStub()
                    }

                    single<PublicApi.ParserPluginController> {
                        ParserPluginControllerStub()
                    }
                }
            )
        }

        // When
        val controller: BananaContract.TopLevelParser = koin.koin.get()

        // Then
        controller fulfils BananaContract.TopLevelParser::class
    }
}
