/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

internal actual class StringReader actual constructor(
    value: String,
) : TokenizerContract.Reader {
    private val reader = value.reader()

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
