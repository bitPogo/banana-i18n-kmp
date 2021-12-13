/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import tech.antibytes.banana.tokenizer.TokenizerContract

internal interface BananaContract {
    enum class TokenTypes {
        DOUBLE,
        INTEGER,
        ESCAPED,
        ASCII_STRING,
        NON_ASCII_STRING,
        LITERAL,
        WHITESPACE,
        VARIABLE,
        DELIMITER,
        FUNCTION_START,
        FUNCTION_END,
        EOF
    }

    data class Token(
        val type: TokenTypes,
        val value: String,
        val column: Int,
        val line: Int
    )

    interface Tokenizer {
        fun setReader(reader: TokenizerContract.Reader)
        fun next(): Token
    }

    interface TokenizerFactory {
        fun getInstance(reader: TokenizerContract.Reader): Tokenizer
    }

    interface TokenStore : Iterator<Token> {
        val currentToken: Token
        val lookahead: Token
        val tokenizer: Tokenizer

        fun shift()
        fun resolveValues(): Array<String>
        fun consume()
    }

    companion object {
        val EOF = Token(TokenTypes.EOF, "", -1, -1)
    }
}
