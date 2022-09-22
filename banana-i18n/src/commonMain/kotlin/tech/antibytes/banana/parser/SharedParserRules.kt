/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.banana.ast.CoreNode.TextNode
import tech.antibytes.banana.ast.CoreNode.VariableNode

abstract class SharedParserRules(
    protected val logger: PublicApi.Logger,
    private val controller: PublicApi.ParserPluginController,
) {
    protected fun logOrConsume(rule: String, tokenizer: PublicApi.TokenStore, condition: () -> Boolean) {
        when {
            condition() -> tokenizer.consume()
            tokenizer.currentToken == BananaContract.EOF -> logger.warning(
                PublicApi.Tag.PARSER,
                "Warning: $rule had not been closed!",
            )
            else -> logger.error(PublicApi.Tag.PARSER, "Error: Unexpected Token (${tokenizer.currentToken})!")
        }
    }

    protected fun shiftUntil(tokenizer: PublicApi.TokenStore, condition: () -> Boolean) {
        do {
            tokenizer.shift()
        } while (condition())
    }

    protected fun isVariable(tokenizer: PublicApi.TokenStore): Boolean {
        return tokenizer.currentToken.isVariable()
    }

    protected fun isFunction(tokenizer: PublicApi.TokenStore): Boolean {
        return tokenizer.currentToken.isFunctionStart() &&
            (
                tokenizer.lookahead.isAscii() ||
                    (
                        tokenizer.lookahead.isSpace() &&
                            tokenizer.lookahead(2).isAscii()
                        )
                )
    }

    protected fun isDelimiter(tokenizer: PublicApi.TokenStore): Boolean {
        return tokenizer.currentToken.isDelimiter() ||
            (
                tokenizer.currentToken.isSpace() &&
                    tokenizer.lookahead.isDelimiter()
                )
    }

    protected fun isEOF(tokenizer: PublicApi.TokenStore): Boolean {
        return tokenizer.currentToken.isEOF() ||
            (
                tokenizer.currentToken.isSpace() &&
                    tokenizer.lookahead.isEOF()
                )
    }

    protected fun isFunctionEndOrDelimiter(tokenizer: PublicApi.TokenStore): Boolean {
        return isDelimiter(tokenizer) ||
            tokenizer.currentToken.isFunctionEnd() ||
            (
                tokenizer.currentToken.isSpace() &&
                    tokenizer.lookahead.isFunctionEnd()
                ) ||
            isEOF(tokenizer)
    }

    protected fun isLink(tokenizer: PublicApi.TokenStore): Boolean {
        return tokenizer.currentToken.isLinkStart() &&
            (
                tokenizer.lookahead.isLinkText() ||
                    tokenizer.lookahead.isVariable() ||
                    (
                        tokenizer.lookahead.isFunctionStart() &&
                            tokenizer.lookahead(2).isAscii() ||
                            (
                                tokenizer.lookahead(2).isSpace() &&
                                    tokenizer.lookahead(3).isAscii()
                                )
                        ) ||
                    tokenizer.lookahead.isSpace() &&
                    (
                        tokenizer.lookahead(2).isLinkText() ||
                            tokenizer.lookahead(2).isVariable() ||
                            (
                                tokenizer.lookahead(2).isFunctionStart() &&
                                    tokenizer.lookahead(3).isAscii() ||
                                    (
                                        tokenizer.lookahead(3).isSpace() &&
                                            tokenizer.lookahead(4).isAscii()
                                        )
                                )
                        )
                )
    }

    protected fun isFreeLink(tokenizer: PublicApi.TokenStore): Boolean {
        return tokenizer.currentToken.isFreeLinkStart() &&
            (
                (tokenizer.lookahead.isUrl() || tokenizer.lookahead.isVariable()) ||
                    (
                        tokenizer.lookahead.isSpace() &&
                            (tokenizer.lookahead(2).isUrl() || tokenizer.lookahead(2).isVariable())
                        )
                )
    }

    private fun isLinkEnd(tokenizer: PublicApi.TokenStore): Boolean {
        return tokenizer.currentToken.isLinkEnd() ||
            (
                tokenizer.currentToken.isSpace() &&
                    tokenizer.lookahead.isLinkEnd()
                ) ||
            isEOF(tokenizer)
    }

    private fun isLinkEndOrDelimiter(tokenizer: PublicApi.TokenStore): Boolean {
        return isDelimiter(tokenizer) ||
            isLinkEnd(tokenizer)
    }

    private fun isFreeLinkDisplay(tokenizer: PublicApi.TokenStore): Boolean {
        return tokenizer.currentToken.isSpace() &&
            !tokenizer.lookahead.isFreeLinkEnd()
    }

    private fun isFreeLinkEnd(tokenizer: PublicApi.TokenStore): Boolean {
        return tokenizer.currentToken.isFreeLinkEnd() ||
            (
                tokenizer.currentToken.isSpace() &&
                    tokenizer.lookahead.isFreeLinkEnd()
                ) ||
            isEOF(tokenizer)
    }

    protected fun variable(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        return VariableNode(
            tokenizer.currentToken.value,
        ).also { tokenizer.consume() }
    }

    protected fun space(tokenizer: PublicApi.TokenStore) {
        if (tokenizer.currentToken.isSpace()) {
            tokenizer.consume()
        }
    }

    private fun linkText(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        shiftUntil(tokenizer) {
            (tokenizer.currentToken.isLinkText() || tokenizer.currentToken.isSpace()) &&
                !isLinkEndOrDelimiter(tokenizer)
        }

        val linkText = tokenizer.resolveValues()

        return TextNode(linkText)
    }

    private fun linkTarget(tokenizer: PublicApi.TokenStore): List<PublicApi.Node> {
        val target = mutableListOf<PublicApi.Node>()

        while (!isLinkEndOrDelimiter(tokenizer)) {
            val part = when {
                isFunction(tokenizer) -> function(tokenizer)
                isVariable(tokenizer) -> variable(tokenizer)
                tokenizer.currentToken.isLinkText() || tokenizer.currentToken.isSpace() -> linkText(tokenizer)
                else -> break // Illegal Literal
            }

            target.add(part)
        }
        return target
    }

    private fun displayText(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        shiftUntil(tokenizer) {
            !isFunction(tokenizer) &&
                !isLinkEnd(tokenizer) &&
                !isVariable(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun linkDisplay(tokenizer: PublicApi.TokenStore): List<PublicApi.Node> {
        val linkDisplay = mutableListOf<PublicApi.Node>()

        while (!isLinkEnd(tokenizer)) {
            val part = when {
                isFunction(tokenizer) -> function(tokenizer)
                isVariable(tokenizer) -> variable(tokenizer)
                else -> displayText(tokenizer)
            }

            linkDisplay.add(part)
        }

        return linkDisplay
    }

    protected fun link(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        tokenizer.consume()
        space(tokenizer)

        val target = linkTarget(tokenizer)

        val displayText = if (isDelimiter(tokenizer)) {
            space(tokenizer)
            tokenizer.consume()
            space(tokenizer)

            linkDisplay(tokenizer)
        } else {
            emptyList()
        }

        space(tokenizer)

        logOrConsume("Link", tokenizer) {
            tokenizer.currentToken.isLinkEnd()
        }

        return CoreNode.LinkNode(target, displayText)
    }

    private fun displayFreeText(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        shiftUntil(tokenizer) {
            !isFunction(tokenizer) &&
                !isFreeLinkEnd(tokenizer) &&
                !isVariable(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun freeLinkDisplay(tokenizer: PublicApi.TokenStore): List<PublicApi.Node> {
        val linkDisplay = mutableListOf<PublicApi.Node>()

        while (!isFreeLinkEnd(tokenizer)) {
            val part = when {
                isFunction(tokenizer) -> function(tokenizer)
                isVariable(tokenizer) -> variable(tokenizer)
                else -> displayFreeText(tokenizer)
            }

            linkDisplay.add(part)
        }

        return linkDisplay
    }

    private fun freeLinkUrl(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        return if (tokenizer.currentToken.isVariable()) {
            variable(tokenizer)
        } else {
            TextNode(
                listOf(tokenizer.currentToken.value),
            ).also { tokenizer.consume() }
        }
    }

    protected fun freeLink(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        tokenizer.consume()
        space(tokenizer)
        val url = freeLinkUrl(tokenizer)

        val linkDisplay = if (isFreeLinkDisplay(tokenizer)) {
            space(tokenizer)
            freeLinkDisplay(tokenizer)
        } else {
            emptyList()
        }

        space(tokenizer)

        logOrConsume("FreeLink", tokenizer) {
            tokenizer.currentToken.isFreeLinkEnd()
        }

        return CoreNode.FreeLinkNode(url, linkDisplay)
    }

    private fun identifier(tokenizer: PublicApi.TokenStore): String {
        do {
            tokenizer.shift()

            if (tokenizer.currentToken.value == "_") {
                tokenizer.shift()
            }
        } while (tokenizer.currentToken.isAscii())

        return tokenizer.resolveValues().joinToString("")
    }

    protected fun nestedText(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        shiftUntil(tokenizer) {
            !isFunction(tokenizer) &&
                !isFunctionEndOrDelimiter(tokenizer) &&
                !isVariable(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun arguments(
        functionId: String,
        tokenizer: PublicApi.TokenStore,
    ): PublicApi.Node {
        tokenizer.consume()
        space(tokenizer)

        val (argument, factory) = controller.resolvePlugin(functionId)

        val arguments = mutableListOf(argument.parse(tokenizer))

        while (isDelimiter(tokenizer)) {
            space(tokenizer)
            tokenizer.consume()
            space(tokenizer)

            arguments.add(
                argument.parse(tokenizer),
            )
        }

        space(tokenizer)
        return factory.createNode(arguments)
    }

    protected fun function(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        tokenizer.consume()
        space(tokenizer)

        val functionId = identifier(tokenizer).uppercase()

        space(tokenizer)

        val function = if (tokenizer.currentToken.isFunctionArgumentIndicator()) {
            FunctionNode(
                functionId,
                arguments(functionId, tokenizer),
            )
        } else {
            FunctionNode(functionId)
        }

        logOrConsume("Function", tokenizer) {
            tokenizer.currentToken.isFunctionEnd()
        }

        return function
    }
}
