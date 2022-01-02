/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

interface PublicApi {
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

    interface ParserPluginController {
        fun resolvePlugin(name: String): Pair<ParserPlugin, NodeFactory>
    }

    interface ParserPluginFactory {
        fun createPlugin(
            logger: Logger,
            plugins: ParserPluginController,
        ): ParserPlugin
    }

    interface Interpreter<T : Node>

    interface TextInterceptor {
        fun intercept(chunk: String): String
    }

    interface InterpreterController : Interpreter<Node> {
        fun interpret(node: Node): String
    }

    interface ParameterizedInterpreterPlugin<T : Node> : Interpreter<T> {
        fun interpret(node: T, controller: InterpreterController): String
    }

    interface InterpreterFactory {
        fun getInstance(logger: Logger, locale: Locale): ParameterizedInterpreterPlugin<out Node>
    }

    interface LinkFormatter {
        fun formatLink(target: String, displayText: String): String
        fun formatFreeLink(url: String, displayText: String): String
    }

    interface Logger {
        fun warning(tag: Tag, message: String)
        fun error(tag: Tag, message: String)
    }

    interface Cache<T : Any> {
        fun contains(key: String): Boolean
        fun getValue(key: String): T
        fun store(key: String, value: T)
    }

    enum class Tag {
        PARSER,
        INTERPRETER
    }

    data class Plugin(
        val name: String,
        val interpreter: InterpreterFactory,
        val parser: Pair<ParserPluginFactory, NodeFactory>? = null,
    )

    interface BananaBuilder {
        fun setLanguage(locale: Locale): BananaBuilder
        fun setTextInterceptor(interceptor: TextInterceptor): BananaBuilder
        fun setLinkFormatter(formatter: LinkFormatter): BananaBuilder
        fun setParserCache(cache: Cache<out Node>): BananaBuilder
        fun setMessageCache(cache: Cache<String>): BananaBuilder
        fun setLogger(logger: Logger): BananaBuilder
        fun registerPlugin(plugin: Plugin): BananaBuilder
        fun clearPlugins(): BananaBuilder
    }

    interface BananaI18n {
        fun i18n(message: String, vararg parameter: String): String
        fun i18n(message: String, parameter: Map<String, String>): String
    }
}
