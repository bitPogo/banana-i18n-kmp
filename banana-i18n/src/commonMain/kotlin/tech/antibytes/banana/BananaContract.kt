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
        LINK_START,
        LINK_END,
        URL,
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

    interface TokenStore {
        val currentToken: Token
        val lookahead: Token
        val tokenizer: Tokenizer

        fun shift()
        fun resolveValues(): List<String>
        fun consume()
        fun lookahead(k: Int): Token
    }

    interface Node

    fun interface ParserPlugin {
        fun parse(tokenizer: TokenStore): Node
    }

    interface TopLevelParser : ParserPlugin

    interface ParserController {
        fun resolveParserPlugin(name: String)
    }

    enum class Tag {
        PARSER
    }

    interface Logger {
        fun info(tag: Tag, message: String)
        fun warning(tag: Tag, message: String)
        fun error(tag: Tag, message: String)
    }

    companion object {
        val EOF = Token(TokenTypes.EOF, "", -1, -1)
    }
}
