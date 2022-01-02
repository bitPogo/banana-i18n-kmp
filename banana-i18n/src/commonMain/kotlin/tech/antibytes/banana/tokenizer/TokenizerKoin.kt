/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import org.koin.core.module.Module
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi

internal fun resolveTokenizerModule(): Module {
    return module {
        factory<TokenizerContract.Reader> { parameter ->
            StringReader(
                parameter.get()
            )
        }

        factory<BananaContract.Tokenizer> { parameter ->
            BananaTokenizer(
                get(parameters = { parameter })
            )
        }


        factory<PublicApi.TokenStore> { parameter ->
            TokenStore(
                get(parameters = { parameter })
            )
        }
    }
}
