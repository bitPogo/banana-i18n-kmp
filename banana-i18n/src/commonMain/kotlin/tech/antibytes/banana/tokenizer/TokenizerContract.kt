/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

internal interface TokenizerContract {
    interface Reader {
        @Throws(Exception::class)
        fun read(charBuffer: CharArray, offset: Int, length: Int): Int

        @Throws(Exception::class)
        fun read(): Int

        @Throws(Exception::class)
        fun close()
    }

    interface Character {
        @Throws(Exception::class)
        fun codePointAt(array: CharArray, index: Int, limit: Int): Int
        fun charCount(codePoint: Int): Int
    }
}
