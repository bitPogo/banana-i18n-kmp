/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.TokenTypes
import tech.antibytes.banana.BananaContract.Node
import tech.antibytes.banana.BananaContract.Token
import tech.antibytes.banana.ast.CompoundNode
import tech.antibytes.banana.ast.MagicWordNode
import tech.antibytes.banana.ast.TextNode
import tech.antibytes.banana.ast.VariableNode

internal class TopLevelParser : BananaContract.TopLevelParser {
    private fun Token.isText(): Boolean {
        return type == TokenTypes.DOUBLE ||
            type == TokenTypes.INTEGER ||
            type == TokenTypes.ASCII_STRING ||
            type == TokenTypes.NON_ASCII_STRING ||
            type == TokenTypes.LITERAL ||
            type == TokenTypes.ESCAPED ||
            type == TokenTypes.WHITESPACE ||
            type == TokenTypes.DELIMITER ||
            type == TokenTypes.FUNCTION_END ||
            type == TokenTypes.LINK_END
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

    private fun text(tokenizer: BananaContract.TokenStore): Node {
        do {
            tokenizer.shift()
        } while (tokenizer.currentToken.isText())

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

    private fun function(tokenizer: BananaContract.TokenStore): Node {
        tokenizer.consume()
        space(tokenizer)

        return MagicWordNode(
            identifier(tokenizer)
        ).also {
            space(tokenizer)
            tokenizer.consume()
        }
    }

    private fun message(tokenizer: BananaContract.TokenStore): List<Node> {
        val nodes: MutableList<Node> = mutableListOf()

        while (!tokenizer.currentToken.isEOF()) {
            val node = when (tokenizer.currentToken.type) {
                TokenTypes.VARIABLE -> variable(tokenizer)
                TokenTypes.FUNCTION_START -> function(tokenizer)
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
}
