/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import java.io.StringReader

internal actual class StringReader actual constructor(
    reader: PlatformStringReader
): TokenizerContract.Reader {
    actual override fun read(charBuffer: CharArray, offset: Int, length: Int): Int {
        TODO("Not yet implemented")
    }

    actual override fun read(): Int {
        TODO("Not yet implemented")
    }

    actual override fun close() {
        TODO("Not yet implemented")
    }
}
