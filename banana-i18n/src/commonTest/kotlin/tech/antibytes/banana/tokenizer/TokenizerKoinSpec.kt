/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import kotlin.test.Test
import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Companion.EOF
import tech.antibytes.banana.PublicApi
import tech.antibytes.mock.tokenizer.TokenizerStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class TokenizerKoinSpec {
    @Test
    fun `Given resolveTokenizerModule is called it contains a Reader, if the appropriate Parameter are delegated`() {
        // Given
        val koin = koinApplication {
            modules(resolveTokenizerModule())
        }

        // When
        val tokenizer: TokenizerContract.Reader = koin.koin.get(
            parameters = {
                parametersOf("something")
            },
        )

        // Then
        tokenizer fulfils TokenizerContract.Reader::class
    }

    @Test
    fun `Given resolveTokenizerModule is called it contains a Tokenizer, if the appropriate Parameter are delegated`() {
        // Given
        var capturedParameter: String? = null
        val message = "Something"

        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveTokenizerModule(),
                module {
                    single<TokenizerContract.Reader> { parameter ->
                        capturedParameter = parameter.get()
                        StringReader(parameter.get())
                    }
                },
            )
        }

        // When
        val tokenizer: BananaContract.Tokenizer = koin.koin.get(
            parameters = {
                parametersOf(message)
            },
        )

        // Then
        tokenizer fulfils BananaContract.Tokenizer::class
        capturedParameter!! mustBe message
    }

    @Test
    fun `Given resolveParserModule is called it contains a TokenStore`() {
        // Given
        var capturedParameter: String? = null
        val message = "Something"

        val koin = koinApplication {
            modules(
                resolveTokenizerModule(),
                module {
                    single<BananaContract.Tokenizer> { parameter ->
                        capturedParameter = parameter.get()
                        TokenizerStub { EOF }
                    }
                },
            )
        }

        // When
        val store: PublicApi.TokenStore = koin.koin.get(
            parameters = {
                parametersOf(message)
            },
        )

        // Then
        store fulfils PublicApi.TokenStore::class
        capturedParameter!! mustBe message
    }
}
