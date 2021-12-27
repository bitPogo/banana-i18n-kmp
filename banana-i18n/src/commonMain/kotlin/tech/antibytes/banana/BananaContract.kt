/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import tech.antibytes.banana.tokenizer.TokenizerContract

interface BananaContract {
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

    interface TokenStoreResetter {
        val tokenizer: Tokenizer

        fun reset()
    }

    interface TokenStore {
        val currentToken: Token
        val lookahead: Token
        fun shift()
        fun resolveValues(): List<String>
        fun consume()
        fun lookahead(k: Int): Token
    }

    interface Node

    fun interface NodeFactory {
        fun createNode(children: List<Node>): Node
    }

    fun interface ParserPlugin {
        fun parse(tokenizer: TokenStore): Node
    }

    fun interface ParserPluginFactory {
        fun createPlugin(
            logger: Logger,
            plugins: ParserPluginController,
        ): ParserPlugin
    }

    interface TopLevelParser : ParserPlugin

    interface ParserPluginController {
        fun resolvePlugin(name: String): Pair<ParserPlugin, NodeFactory>
    }

    interface InterpreterPlugin<T : Node> {
        fun interpret(Node: T): String
    }

    fun interface TextInterceptor {
        fun intercept(chunk: String): String
    }

    enum class Tag {
        PARSER
    }

    interface Logger {
        fun warning(tag: Tag, message: String)
        fun error(tag: Tag, message: String)
    }

    companion object {
        val EOF = Token(TokenTypes.EOF, "", -1, -1)
    }
}
