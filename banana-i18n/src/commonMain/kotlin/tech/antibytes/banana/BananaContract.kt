/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import tech.antibytes.banana.tokenizer.TokenizerContract

internal interface BananaContract {
    enum class TokenTypes {
        RULE_OPENING,
        RULE_CLOSURE,
        DOUBLE,
        INTEGER,
        ESCAPED,
        DELIMITER,
        ASCII_STRING,
        NON_ASCII_STRING,
        LITERAL,
        WHITESPACE,
        VARIABLE,
        EOF
    }

    data class Token(
        val type: TokenTypes,
        val value: String,
        val start: Int,
        val end: Int
    )

    interface Tokenizer {
        fun setReader(reader: TokenizerContract.Reader)
        fun next(): Token
    }

    interface TokenizerFactory {
        fun getInstance(reader: TokenizerContract.Reader): Tokenizer
    }

    companion object {
        val EOF = Token(TokenTypes.EOF, "", -1, -1)
    }
}
