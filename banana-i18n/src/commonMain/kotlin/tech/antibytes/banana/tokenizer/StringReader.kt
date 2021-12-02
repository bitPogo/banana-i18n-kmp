/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

internal expect class PlatformStringReader

internal expect class StringReader(
    reader: PlatformStringReader
) : TokenizerContract.Reader {
    override fun read(charBuffer: CharArray, offset: Int, length: Int): Int

    override fun read(): Int

    override fun close()
}
