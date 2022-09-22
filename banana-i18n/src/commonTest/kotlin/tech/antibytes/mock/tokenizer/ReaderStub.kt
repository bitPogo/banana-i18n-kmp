/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.tokenizer

import tech.antibytes.banana.tokenizer.TokenizerContract
import tech.antibytes.util.test.MockContract

class ReaderStub : TokenizerContract.Reader, MockContract.Mock {
    override fun read(buffer: CharArray, offset: Int, limit: Int): Int {
        TODO("Not yet implemented")
    }

    override fun read(): Int {
        TODO("Not yet implemented")
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun clear() {
        TODO("Not yet implemented")
    }
}
