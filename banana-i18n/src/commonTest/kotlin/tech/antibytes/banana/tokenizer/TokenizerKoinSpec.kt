/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import org.koin.dsl.koinApplication
import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class TokenizerKoinSpec {
    @Test
    fun `Given resolveTokenizerModule is called it contains a TokenizerFactory`() {
        // Given
        val koin = koinApplication {
            modules(resolveTokenizerModule())
        }

        // When
        val tokenizer: BananaContract.TokenizerFactory = koin.koin.get()

        // Then
        tokenizer fulfils BananaContract.TokenizerFactory::class
    }
}
