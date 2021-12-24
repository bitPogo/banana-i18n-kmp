/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CompoundNode
import tech.antibytes.banana.ast.FunctionNode
import tech.antibytes.banana.ast.TextNode
import tech.antibytes.banana.ast.VariableNode

abstract class SharedParserRules(
    protected val logger: BananaContract.Logger
) {
    protected fun logOrConsume(rule: String, tokenizer: BananaContract.TokenStore, condition: () -> Boolean) {
        when {
            condition() -> tokenizer.consume()
            tokenizer.currentToken == BananaContract.EOF -> logger.warning(
                BananaContract.Tag.PARSER,
                "Warning: $rule had not been closed!"
            )
            else -> logger.error(BananaContract.Tag.PARSER, "Error: Unexpected Token (${tokenizer.currentToken})!")
        }
    }

    protected fun isVariable(tokenizer: BananaContract.TokenStore): Boolean {
        return tokenizer.currentToken.isVariable()
    }

    protected fun isFunction(tokenizer: BananaContract.TokenStore): Boolean {
        return tokenizer.currentToken.isFunctionStart() &&
            (tokenizer.lookahead.isAscii() ||
                (tokenizer.lookahead.isSpace() &&
                    tokenizer.lookahead(2).isAscii()
                )
            )
    }

    protected fun isDelimiter(tokenizer: BananaContract.TokenStore): Boolean {
        return tokenizer.currentToken.isDelimiter() ||
            (tokenizer.currentToken.isSpace() &&
                tokenizer.lookahead.isDelimiter()
            )
    }

    protected fun isEOF(tokenizer: BananaContract.TokenStore): Boolean {
        return tokenizer.currentToken.isEOF() ||
            (tokenizer.currentToken.isSpace() &&
                tokenizer.lookahead.isEOF()
            )
    }

    protected fun shiftUntil(tokenizer: BananaContract.TokenStore, condition: () -> Boolean) {
        do {
            tokenizer.shift()
        } while (condition())
    }

    protected fun isFunctionEndOrDelimiter(tokenizer: BananaContract.TokenStore): Boolean {
        return isDelimiter(tokenizer) ||
            tokenizer.currentToken.isFunctionEnd() ||
            (tokenizer.currentToken.isSpace() &&
                tokenizer.lookahead.isFunctionEnd()
            ) ||
            isEOF(tokenizer)
    }

    protected fun variable(tokenizer: BananaContract.TokenStore): BananaContract.Node {
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

    protected fun space(tokenizer: BananaContract.TokenStore) {
        if (tokenizer.currentToken.isSpace()) {
            tokenizer.consume()
        }
    }

    protected fun nestedText(tokenizer: BananaContract.TokenStore): BananaContract.Node {
        shiftUntil(tokenizer) {
            !isFunction(tokenizer) &&
                !isFunctionEndOrDelimiter(tokenizer) &&
                !isVariable(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun argument(tokenizer: BananaContract.TokenStore): BananaContract.Node {
        val argument = mutableListOf<BananaContract.Node>()

        while (!isFunctionEndOrDelimiter(tokenizer)) {
            val partialArgument = when {
                isFunction(tokenizer) -> function(tokenizer)
                isVariable(tokenizer) -> variable(tokenizer)
                else -> nestedText(tokenizer)
            }

            argument.add(partialArgument)
        }

        return CompoundNode(argument)
    }

    private fun arguments(tokenizer: BananaContract.TokenStore): List<BananaContract.Node> {
        tokenizer.consume()
        space(tokenizer)

        val arguments = mutableListOf(argument(tokenizer))

        while (isDelimiter(tokenizer)) {
            space(tokenizer)
            tokenizer.consume()
            space(tokenizer)

            arguments.add(argument(tokenizer))
        }

        space(tokenizer)
        return arguments
    }

    protected fun function(tokenizer: BananaContract.TokenStore): BananaContract.Node {
        tokenizer.consume()
        space(tokenizer)

        val functionName = identifier(tokenizer)

        space(tokenizer)

        val arguments = if (tokenizer.currentToken.isFunctionArgumentIndicator()) {
            arguments(tokenizer)
        } else {
            emptyList()
        }

        logOrConsume("Function", tokenizer) {
            tokenizer.currentToken.isFunctionEnd()
        }

        return FunctionNode(functionName, arguments)
    }
}
