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
import tech.antibytes.banana.ast.CoreNode.TextNode

internal class BananaParser(
    logger: PublicApi.Logger,
    parserPluginController: PublicApi.ParserPluginController
) : BananaContract.TopLevelParser, SharedParserRules(logger, parserPluginController) {
    private fun text(tokenizer: PublicApi.TokenStore): Node {
        shiftUntil(tokenizer) {
            !tokenizer.currentToken.isEOF() &&
                !isFunction(tokenizer) &&
                !isLink(tokenizer) &&
                !isFreeLink(tokenizer) &&
                !isVariable(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun message(tokenizer: PublicApi.TokenStore): List<Node> {
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

    override fun parse(tokenizer: PublicApi.TokenStore): Node {
        val tokens = if (tokenizer.currentToken.isEOF()) {
            emptyList()
        } else {
            message(tokenizer)
        }

        return CompoundNode(tokens)
    }
}
