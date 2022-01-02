/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.PublicApi.Node
import tech.antibytes.banana.ast.CoreNode.CompoundNode
import tech.antibytes.banana.ast.CoreNode.FreeLinkNode
import tech.antibytes.banana.ast.CoreNode.LinkNode
import tech.antibytes.banana.ast.CoreNode.TextNode

internal class BananaParser(
    logger: PublicApi.Logger,
    parserPluginController: PublicApi.ParserPluginController
) : BananaContract.TopLevelParser, SharedParserRules(logger, parserPluginController) {
    private fun isLink(tokenizer: PublicApi.ParserEngine): Boolean {
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

    private fun isFreeLink(tokenizer: PublicApi.ParserEngine): Boolean {
        return tokenizer.currentToken.isFreeLinkStart() &&
            (
                (tokenizer.lookahead.isUrl() || tokenizer.lookahead.isVariable()) ||
                    (
                        tokenizer.lookahead.isSpace() &&
                            (tokenizer.lookahead(2).isUrl() || tokenizer.lookahead(2).isVariable())
                        )
                )
    }

    private fun isLinkEnd(tokenizer: PublicApi.ParserEngine): Boolean {
        return tokenizer.currentToken.isLinkEnd() ||
            (
                tokenizer.currentToken.isSpace() &&
                    tokenizer.lookahead.isLinkEnd()
                ) ||
            isEOF(tokenizer)
    }

    private fun isLinkEndOrDelimiter(tokenizer: PublicApi.ParserEngine): Boolean {
        return isDelimiter(tokenizer) ||
            isLinkEnd(tokenizer)
    }

    private fun isFreeLinkDisplay(tokenizer: PublicApi.ParserEngine): Boolean {
        return tokenizer.currentToken.isSpace() &&
            !tokenizer.lookahead.isFreeLinkEnd()
    }

    private fun isFreeLinkEnd(tokenizer: PublicApi.ParserEngine): Boolean {
        return tokenizer.currentToken.isFreeLinkEnd() ||
            (
                tokenizer.currentToken.isSpace() &&
                    tokenizer.lookahead.isFreeLinkEnd()
                ) ||
            isEOF(tokenizer)
    }

    private fun text(tokenizer: PublicApi.ParserEngine): Node {
        shiftUntil(tokenizer) {
            !tokenizer.currentToken.isEOF() &&
                !isFunction(tokenizer) &&
                !isLink(tokenizer) &&
                !isFreeLink(tokenizer) &&
                !isVariable(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun linkText(tokenizer: PublicApi.ParserEngine): Node {
        shiftUntil(tokenizer) {
            (tokenizer.currentToken.isLinkText() || tokenizer.currentToken.isSpace()) &&
                !isLinkEndOrDelimiter(tokenizer)
        }

        val linkText = tokenizer.resolveValues()

        return TextNode(linkText)
    }

    private fun linkTarget(tokenizer: PublicApi.ParserEngine): List<Node> {
        val target = mutableListOf<Node>()

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

    private fun displayText(tokenizer: PublicApi.ParserEngine): Node {
        shiftUntil(tokenizer) {
            !isFunction(tokenizer) &&
                !isLinkEnd(tokenizer) &&
                !isVariable(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun linkDisplay(tokenizer: PublicApi.ParserEngine): List<Node> {
        val linkDisplay = mutableListOf<Node>()

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

    private fun link(tokenizer: PublicApi.ParserEngine): Node {
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

        return LinkNode(target, displayText)
    }

    private fun displayFreeText(tokenizer: PublicApi.ParserEngine): Node {
        shiftUntil(tokenizer) {
            !isFunction(tokenizer) &&
                !isFreeLinkEnd(tokenizer) &&
                !isVariable(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun freeLinkDisplay(tokenizer: PublicApi.ParserEngine): List<Node> {
        val linkDisplay = mutableListOf<Node>()

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

    private fun freeLinkUrl(tokenizer: PublicApi.ParserEngine): Node {
        return if (tokenizer.currentToken.isVariable()) {
            variable(tokenizer)
        } else {
            TextNode(
                listOf(tokenizer.currentToken.value)
            ).also { tokenizer.consume() }
        }
    }

    private fun freeLink(tokenizer: PublicApi.ParserEngine): Node {
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

        return FreeLinkNode(url, linkDisplay)
    }

    private fun message(tokenizer: PublicApi.ParserEngine): List<Node> {
        val nodes: MutableList<Node> = mutableListOf()

        while (!tokenizer.currentToken.isEOF()) {
            val node = when {
                isVariable(tokenizer) -> variable(tokenizer)
                isFunction(tokenizer) -> function(tokenizer)
                isLink(tokenizer) -> link(tokenizer)
                isFreeLink(tokenizer) -> freeLink(tokenizer)
                else -> text(tokenizer)
            }

            nodes.add(node)
        }

        return nodes
    }

    override fun parse(tokenizer: PublicApi.ParserEngine): Node {
        val tokens = if (tokenizer.currentToken.isEOF()) {
            emptyList()
        } else {
            message(tokenizer)
        }

        return CompoundNode(tokens)
    }
}
