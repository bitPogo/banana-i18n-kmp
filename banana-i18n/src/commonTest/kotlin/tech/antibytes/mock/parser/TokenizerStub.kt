/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.tokenizer.TokenizerContract.Reader
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class TokenizerStub(
    var setReader: ((reader: Reader) -> Unit)? = null,
    var next: (() -> BananaContract.Token)? = null
) : BananaContract.Tokenizer, MockContract.Mock {
    override fun setReader(reader: Reader) {
        return setReader?.invoke(reader)
            ?: throw MockError.MissingStub("Missing sideeffect for setReader")
    }

    override fun next(): BananaContract.Token {
        return next?.invoke()
            ?: throw MockError.MissingStub("Missing next for setReader")
    }

    override fun clear() {
        setReader = null
        next = null
    }
}
