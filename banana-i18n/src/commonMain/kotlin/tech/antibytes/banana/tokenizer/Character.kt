/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

internal expect object Character : TokenizerContract.Character {
    override fun codePointAt(array: CharArray, index: Int, limit: Int): Int

    override fun charCount(codePoint: Int): Int
}
