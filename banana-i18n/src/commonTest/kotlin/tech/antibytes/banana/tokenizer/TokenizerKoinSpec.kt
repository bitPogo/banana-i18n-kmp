/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import org.koin.core.parameter.parametersOf
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class TokenizerKoinSpec {
    @Test
    fun `Given resolveTokenizerModule is called it contains a Reader, if the appropriate Parameter is delegated`() {
        // Given
        val koin = koinApplication {
            modules(resolveTokenizerModule())
        }

        // When
        val tokenizer: TokenizerContract.Reader = koin.koin.get(
            parameters = {
                parametersOf("something")
            }
        )

        // Then
        tokenizer fulfils TokenizerContract.Reader::class
    }

    @Test
    fun `Given resolveTokenizerModule is called it contains a Tokenizer`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveTokenizerModule(),
                module {
                    single<TokenizerContract.Reader> {
                        StringReader("")
                    }
                }
            )

        }

        // When
        val tokenizer: BananaContract.Tokenizer = koin.koin.get()

        // Then
        tokenizer fulfils BananaContract.Tokenizer::class
    }
}
