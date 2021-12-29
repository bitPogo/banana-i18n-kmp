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

    interface NodeFactory {
        fun createNode(children: List<Node>): Node
    }

    interface ParserPlugin {
        fun parse(tokenizer: TokenStore): Node
    }

    interface ParserPluginFactory {
        fun createPlugin(
            logger: Logger,
            plugins: ParserPluginController,
        ): ParserPlugin
    }

    interface TopLevelParser : ParserPlugin

    interface ParserPluginController {
        fun resolvePlugin(name: String): Pair<ParserPlugin, NodeFactory>
    }

    interface Interpreter<T : Node>

    interface InterpreterPlugin<T : Node> : Interpreter<T> {
        fun interpret(node: T): String
    }

    interface ParameterizedInterpreterPlugin<T : Node, P : Any> : Interpreter<T> {
        fun interpret(node: T, parameter: P): String
    }

    interface TextInterceptor {
        fun intercept(chunk: String): String
    }

    interface LinkFormatter {
        fun formatLink(target: String, displayText: String): String
        fun formatFreeLink(url: String, displayText: String): String
    }

    interface InterpreterController {
        fun interpret(node: Node): String
    }

    enum class Tag {
        PARSER,
        INTERPRETER
    }

    interface Logger {
        fun warning(tag: Tag, message: String)
        fun error(tag: Tag, message: String)
    }

    companion object {
        val EOF = Token(TokenTypes.EOF, "", -1, -1)
    }
}
