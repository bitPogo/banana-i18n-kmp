/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import tech.antibytes.banana.BananaRuntimeError

sealed class TokenizerError(
    override val message: String,
) : BananaRuntimeError(message) {
    class IllegalCharacter(message: String) : BananaRuntimeError(message)
    class UnknownState(message: String) : BananaRuntimeError(message)
}
