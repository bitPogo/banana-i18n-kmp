/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import java.lang.Character as JCharacter

internal actual object Character : TokenizerContract.Character {
    actual override fun codePointAt(
        array: CharArray,
        index: Int,
        limit: Int
    ): Int = JCharacter.codePointAt(array, index, limit)

    actual override fun charCount(codePoint: Int): Int = JCharacter.charCount(codePoint)
}
