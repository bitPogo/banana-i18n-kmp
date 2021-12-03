/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.tokenizer

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Token

internal class BananaTokenizer private constructor(
    reader: TokenizerContract.Reader
) : BananaContract.Tokenizer, BananaFlexTokenizer(reader) {
    override fun setInputStream(stream: String) {
        TODO("Not yet implemented")
    }

    override fun next(): Token {
        return if (yyatEOF()) {
            BananaContract.EOF
        } else {
            yylex() ?: BananaContract.EOF
        }
    }

    companion object : BananaContract.TokenizerFactory {
        override fun getInstance(reader: TokenizerContract.Reader): BananaContract.Tokenizer = BananaTokenizer(reader)
    }
}