/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */
package tech.antibytes.banana.tokenizer

import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class BananaTokenizerSpec {
    @Test
    fun `It fulfils Tokenizer`() {
        Character fulfils TokenizerContract.Character::class
    }
}
