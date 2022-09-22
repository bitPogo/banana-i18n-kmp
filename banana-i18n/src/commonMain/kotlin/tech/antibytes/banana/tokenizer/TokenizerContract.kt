/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

interface TokenizerContract {
    interface Reader {
        @Throws(Exception::class)
        fun read(buffer: CharArray, offset: Int, limit: Int): Int

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
