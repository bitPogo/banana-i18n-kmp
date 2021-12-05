/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

internal expect class StringReader(
    value: String
) : TokenizerContract.Reader {
    override fun read(buffer: CharArray, offset: Int, limit: Int): Int

    override fun read(): Int

    override fun close()
}
