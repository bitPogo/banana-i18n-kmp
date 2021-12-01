/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import tech.antibytes.test.utils.fulfils
import kotlin.test.Test

class StringReaderSpec {
    @Test
    fun `It fulfils Reader`() {
        StringReader() fulfils TokenizerContract.Reader::class
    }
}
