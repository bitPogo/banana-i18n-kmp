/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import org.koin.core.module.Module
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract

internal fun resolveTokenizer(): Module {
    return module {
        single<BananaContract.TokenizerFactory> {
            BananaTokenizer
        }
    }
}
