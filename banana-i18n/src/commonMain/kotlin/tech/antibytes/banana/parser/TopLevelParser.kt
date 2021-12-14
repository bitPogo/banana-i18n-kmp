/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Companion.EOF
import tech.antibytes.banana.BananaContract.TokenTypes
import tech.antibytes.banana.BananaContract.Node
import tech.antibytes.banana.BananaContract.Token
import tech.antibytes.banana.ast.CompoundNode
import tech.antibytes.banana.ast.HeadlessLinkNode
import tech.antibytes.banana.ast.FunctionNode
import tech.antibytes.banana.ast.TextNode
import tech.antibytes.banana.ast.VariableNode
import tech.antibytes.banana.BananaContract.Tag
import tech.antibytes.banana.ast.HeadlessFreeLinkNode

internal class TopLevelParser(
    private val logger: BananaContract.Logger
) : BananaContract.TopLevelParser {
    private fun Token.isText(): Boolean {
        return type == TokenTypes.DOUBLE ||
            type == TokenTypes.INTEGER ||
            type == TokenTypes.ASCII_STRING ||
            type == TokenTypes.NON_ASCII_STRING ||
            type == TokenTypes.LITERAL ||
            type == TokenTypes.ESCAPED ||
            type == TokenTypes.WHITESPACE ||
            type == TokenTypes.FUNCTION_END ||
            type == TokenTypes.DELIMITER ||
            type == TokenTypes.LINK_END ||
            type == TokenTypes.FUNCTION_START ||
            type == TokenTypes.LINK_START ||
            type == TokenTypes.URL
    }

    private fun Token.isLinkText(): Boolean {
        return (type == TokenTypes.DOUBLE ||
            type == TokenTypes.INTEGER ||
            type == TokenTypes.ASCII_STRING ||
            type == TokenTypes.NON_ASCII_STRING ||
            type == TokenTypes.LITERAL ||
            type == TokenTypes.ESCAPED ||
            type == TokenTypes.WHITESPACE) &&
            !INVALID_LINK_LITERAL.contains(value)
    }

    private fun Token.isNestedText(): Boolean {
        return type == TokenTypes.DOUBLE ||
            type == TokenTypes.INTEGER ||
            type == TokenTypes.ASCII_STRING ||
            type == TokenTypes.NON_ASCII_STRING ||
            type == TokenTypes.LITERAL ||
            type == TokenTypes.ESCAPED ||
            type == TokenTypes.WHITESPACE ||
            type == TokenTypes.LINK_END ||
            type == TokenTypes.FUNCTION_START ||
            type == TokenTypes.LINK_START ||
            type == TokenTypes.URL
    }

    private fun Token.isVariable(): Boolean {
        return type == TokenTypes.VARIABLE
    }

    private fun Token.isAscii(): Boolean {
        return type == TokenTypes.ASCII_STRING
    }

    private fun Token.isSpace(): Boolean {
        return type == TokenTypes.WHITESPACE
    }

    private fun Token.isFunctionStart(): Boolean {
        return type == TokenTypes.FUNCTION_START
    }

    private fun Token.isFunctionEnd(): Boolean {
        return type == TokenTypes.FUNCTION_END
    }

    private fun Token.isLinkStart(): Boolean {
        return type == TokenTypes.LINK_START
    }

    private fun Token.isLinkEnd(): Boolean {
        return type == TokenTypes.LINK_END
    }

    private fun Token.isFreeLinkStart(): Boolean {
        return value == "["
    }

    private fun Token.isFreeLinkEnd(): Boolean {
        return value == "]"
    }

    private fun Token.isUrl(): Boolean {
        return type == TokenTypes.URL
    }

    private fun Token.isFunctionArgumentIndicator(): Boolean {
        return value == ":"
    }

    private fun Token.isDelimiter(): Boolean {
        return value == "|"
    }

    private fun isFunction(tokenizer: BananaContract.TokenStore): Boolean {
        return tokenizer.currentToken.isFunctionStart() &&
            (tokenizer.lookahead.isAscii() ||
                (tokenizer.lookahead.isSpace() && tokenizer.lookahead(2).isAscii()))
    }

    private fun isLink(tokenizer: BananaContract.TokenStore): Boolean {
        return tokenizer.currentToken.isLinkStart() &&
            (tokenizer.lookahead.isAscii() ||
                (tokenizer.lookahead.isSpace() && tokenizer.lookahead(2).isLinkText()))
    }

    private fun isFreeLink(tokenizer: BananaContract.TokenStore): Boolean {
        return tokenizer.currentToken.isFreeLinkStart() &&
            (tokenizer.lookahead.isUrl() ||
                (tokenizer.lookahead.isSpace() && tokenizer.lookahead(2).isUrl()))
    }

    private fun isDelimiter(tokenizer: BananaContract.TokenStore): Boolean {
        return tokenizer.currentToken.isDelimiter() ||
            (tokenizer.currentToken.isSpace() && tokenizer.lookahead.isDelimiter())
    }

    private fun isFunctionEndOrDelimiter(tokenizer: BananaContract.TokenStore) : Boolean {
        return isDelimiter(tokenizer) ||
            tokenizer.currentToken.isFunctionEnd() ||
            (tokenizer.currentToken.isSpace() && tokenizer.lookahead.isFunctionEnd())
    }

    private fun shiftUntil(tokenizer: BananaContract.TokenStore, condition: () -> Boolean) {
        do {
            tokenizer.shift()
        } while (condition())
    }

    private enum class LogLevel {
        INFO,
        WARNING,
        ERROR
    }

    private fun log(message: String, level: LogLevel = LogLevel.WARNING) {
        when (level) {
            LogLevel.INFO -> logger.info(Tag.PARSER, message)
            LogLevel.WARNING -> logger.warning(Tag.PARSER, message)
            else -> logger.error(Tag.PARSER, message)
        }
    }

    private fun text(tokenizer: BananaContract.TokenStore): Node {
        shiftUntil(tokenizer) {
            tokenizer.currentToken.isText() && !isFunction(tokenizer) && !isLink(tokenizer) && !isFreeLink(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun variable(tokenizer: BananaContract.TokenStore): Node {
        return VariableNode(
            tokenizer.currentToken.value
        ).also { tokenizer.consume() }
    }

    private fun identifier(tokenizer: BananaContract.TokenStore): String {
        do {
            tokenizer.shift()

            if (tokenizer.currentToken.value == "_") {
                tokenizer.shift()
            }
        } while (tokenizer.currentToken.isAscii())

        return tokenizer.resolveValues().joinToString("")
    }

    private fun space(tokenizer: BananaContract.TokenStore) {
        if (tokenizer.currentToken.isSpace()) {
            tokenizer.consume()
        }
    }

    private fun nestedText(tokenizer: BananaContract.TokenStore): Node {
        shiftUntil(tokenizer) {
            tokenizer.currentToken.isNestedText() && !isFunction(tokenizer) && !isFunctionEndOrDelimiter(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun argument(tokenizer: BananaContract.TokenStore): Node {
        val argument = mutableListOf<Node>()

        while (!isFunctionEndOrDelimiter(tokenizer)) {
            val partialArgument = when {
                isFunction(tokenizer) -> function(tokenizer)
                tokenizer.currentToken.isVariable() -> variable(tokenizer)
                else -> nestedText(tokenizer)
            }

            argument.add(partialArgument)
        }

        return CompoundNode(argument)
    }

    private fun arguments(tokenizer: BananaContract.TokenStore): List<Node> {
        tokenizer.consume()
        space(tokenizer)

        val argument = argument(tokenizer)

        space(tokenizer)
        return listOf(argument)
    }

    private fun function(tokenizer: BananaContract.TokenStore): Node {
        tokenizer.consume()
        space(tokenizer)

        val functionName = identifier(tokenizer)

        space(tokenizer)

        val arguments = if (tokenizer.currentToken.isFunctionArgumentIndicator()) {
            arguments(tokenizer)
        } else {
            emptyList()
        }

        if (!tokenizer.currentToken.isFunctionEnd()) {
            log("Warning: Function ($functionName) had not been closed!")
        } else {
            tokenizer.consume()
        }

        return FunctionNode(functionName, arguments)
    }

    private fun linkText(tokenizer: BananaContract.TokenStore): String {
        shiftUntil(tokenizer) {
            tokenizer.currentToken.isLinkText() && !isFunction(tokenizer)
        }

        return tokenizer.resolveValues().joinToString("").trimEnd()
    }

    private fun link(tokenizer: BananaContract.TokenStore): Node {
        tokenizer.consume()
        space(tokenizer)
        val linkName = linkText(tokenizer)
        space(tokenizer)

        when {
            tokenizer.currentToken.isLinkEnd() -> tokenizer.consume()
            tokenizer.currentToken == EOF -> log("Warning: Link ($linkName) had not been closed!")
            else -> log("Error: Unexpected Token (${tokenizer.currentToken}) in Link ($linkName)!", LogLevel.ERROR)
        }

        return HeadlessLinkNode(linkName)
    }

    private fun freeLink(tokenizer: BananaContract.TokenStore): Node {
        tokenizer.consume()
        space(tokenizer)
        val url = tokenizer.currentToken.value
        tokenizer.consume()
        space(tokenizer)

        if (!tokenizer.currentToken.isFreeLinkEnd()) {
            log("Warning: FreeLink ($url) had not been closed!")
        } else {
            tokenizer.consume()
        }

        return HeadlessFreeLinkNode(url)
    }

    private fun message(tokenizer: BananaContract.TokenStore): List<Node> {
        val nodes: MutableList<Node> = mutableListOf()

        while (!tokenizer.currentToken.isEOF()) {
            val node = when {
                tokenizer.currentToken.isVariable() -> variable(tokenizer)
                isFunction(tokenizer) -> function(tokenizer)
                isLink(tokenizer) -> link(tokenizer)
                isFreeLink(tokenizer) -> freeLink(tokenizer)
                else -> text(tokenizer)
            }

            nodes.add(node)
        }

        return nodes
    }

    override fun parse(tokenizer: BananaContract.TokenStore): Node {
        val tokens = if (tokenizer.currentToken.isEOF()) {
            emptyList()
        } else {
            message(tokenizer)
        }

        return CompoundNode(tokens)
    }

    private companion object {
        val INVALID_LINK_LITERAL = listOf("{", "[", "}", "]")
    }
}
