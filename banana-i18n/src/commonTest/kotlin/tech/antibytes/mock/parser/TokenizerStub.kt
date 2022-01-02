/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.tokenizer.TokenizerContract.Reader
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class TokenizerStub(
    var setReader: ((reader: Reader) -> Unit)? = null,
    var next: (() -> PublicApi.Token)? = null
) : BananaContract.Tokenizer, MockContract.Mock {
    override fun next(): PublicApi.Token {
        return next?.invoke()
            ?: throw MockError.MissingStub("Missing next for setReader")
    }

    override fun clear() {
        setReader = null
        next = null
    }
}
