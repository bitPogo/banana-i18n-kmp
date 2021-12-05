/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import java.io.StringReader

internal actual class StringReader actual constructor(
    value: String
) : TokenizerContract.Reader {
    private val reader: StringReader = value.reader()

    actual override fun read(buffer: CharArray, offset: Int, limit: Int): Int {
        return reader.read(buffer, offset, limit)
    }

    actual override fun read(): Int {
        return reader.read()
    }

    actual override fun close() {
        return reader.close()
    }
}
