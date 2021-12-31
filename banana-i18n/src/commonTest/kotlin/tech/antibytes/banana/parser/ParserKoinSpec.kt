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
import tech.antibytes.mock.parser.TokenizerStub
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class ParserKoinSpec {
    @Test
    fun `Given resolveParserKoin is called it contains a TokenStore`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveParserKoin(),
                module {
                    single<BananaContract.Tokenizer> {
                        TokenizerStub(next = { BananaContract.EOF })
                    }
                }
            )
        }

        // When
        val store: PublicApi.TokenStore = koin.koin.get()

        // Then
        store fulfils PublicApi.TokenStore::class
    }

    @Test
    fun `Given resolveParserKoin is called it contains a DefaultArgumentParser Pair`() {
        // Given
        val koin = koinApplication {
            modules(
                resolveParserKoin(),
                module {
                    single<PublicApi.NodeFactory>(named(BananaContract.KoinLabel.COMPOUND_FACTORY)) {
                        NodeFactoryStub()
                    }
                }
            )
        }

        // When
        val default: Pair<PublicApi.ParserPluginFactory, PublicApi.NodeFactory> = koin.koin.get(
            named(BananaContract.KoinLabel.DEFAULT_ARGUMENT_PARSER)
        )

        // Then
        default.first fulfils DefaultArgumentsParser.Companion::class
        default.second fulfils NodeFactoryStub::class
    }

    @Test
    fun `Given resolveParserKoin is called it contains a ParserPluginController`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveParserKoin(),
                module {
                    single<Map<String, Pair<PublicApi.ParserPluginFactory, PublicApi.NodeFactory>>>(named(BananaContract.KoinLabel.PARSER_PLUGINS)) {
                        emptyMap()
                    }

                    single<PublicApi.Logger> {
                        LoggerStub()
                    }

                    single<Pair<PublicApi.ParserPluginFactory, PublicApi.NodeFactory>>(named(BananaContract.KoinLabel.DEFAULT_ARGUMENT_PARSER)) {
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
    fun `Given resolveParserKoin is called it contains a BananaParser`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveParserKoin(),
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
