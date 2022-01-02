/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.banana.ast.CoreNode.TextNode
import tech.antibytes.banana.ast.CoreNode.VariableNode

abstract class SharedParserRules(
    protected val logger: PublicApi.Logger,
    private val plugins: PublicApi.ParserPluginController
) {
    protected fun logOrConsume(rule: String, tokenizer: PublicApi.ParserEngine, condition: () -> Boolean) {
        when {
            condition() -> tokenizer.consume()
            tokenizer.currentToken == BananaContract.EOF -> logger.warning(
                PublicApi.Tag.PARSER,
                "Warning: $rule had not been closed!"
            )
            else -> logger.error(PublicApi.Tag.PARSER, "Error: Unexpected Token (${tokenizer.currentToken})!")
        }
    }

    protected fun isVariable(tokenizer: PublicApi.ParserEngine): Boolean {
        return tokenizer.currentToken.isVariable()
    }

    protected fun isFunction(tokenizer: PublicApi.ParserEngine): Boolean {
        return tokenizer.currentToken.isFunctionStart() &&
            (
                tokenizer.lookahead.isAscii() ||
                    (
                        tokenizer.lookahead.isSpace() &&
                            tokenizer.lookahead(2).isAscii()
                        )
                )
    }

    protected fun isDelimiter(tokenizer: PublicApi.ParserEngine): Boolean {
        return tokenizer.currentToken.isDelimiter() ||
            (
                tokenizer.currentToken.isSpace() &&
                    tokenizer.lookahead.isDelimiter()
                )
    }

    protected fun isEOF(tokenizer: PublicApi.ParserEngine): Boolean {
        return tokenizer.currentToken.isEOF() ||
            (
                tokenizer.currentToken.isSpace() &&
                    tokenizer.lookahead.isEOF()
                )
    }

    protected fun shiftUntil(tokenizer: PublicApi.ParserEngine, condition: () -> Boolean) {
        do {
            tokenizer.shift()
        } while (condition())
    }

    protected fun isFunctionEndOrDelimiter(tokenizer: PublicApi.ParserEngine): Boolean {
        return isDelimiter(tokenizer) ||
            tokenizer.currentToken.isFunctionEnd() ||
            (
                tokenizer.currentToken.isSpace() &&
                    tokenizer.lookahead.isFunctionEnd()
                ) ||
            isEOF(tokenizer)
    }

    protected fun variable(tokenizer: PublicApi.ParserEngine): PublicApi.Node {
        return VariableNode(
            tokenizer.currentToken.value
        ).also { tokenizer.consume() }
    }

    private fun identifier(tokenizer: PublicApi.ParserEngine): String {
        do {
            tokenizer.shift()

            if (tokenizer.currentToken.value == "_") {
                tokenizer.shift()
            }
        } while (tokenizer.currentToken.isAscii())

        return tokenizer.resolveValues().joinToString("")
    }

    protected fun space(tokenizer: PublicApi.ParserEngine) {
        if (tokenizer.currentToken.isSpace()) {
            tokenizer.consume()
        }
    }

    protected fun nestedText(tokenizer: PublicApi.ParserEngine): PublicApi.Node {
        shiftUntil(tokenizer) {
            !isFunction(tokenizer) &&
                !isFunctionEndOrDelimiter(tokenizer) &&
                !isVariable(tokenizer)
        }

        return TextNode(tokenizer.resolveValues())
    }

    private fun arguments(
        functionId: String,
        tokenizer: PublicApi.ParserEngine
    ): PublicApi.Node {
        tokenizer.consume()
        space(tokenizer)

        val (argument, factory) = plugins.resolvePlugin(functionId)

        val arguments = mutableListOf(argument.parse(tokenizer))

        while (isDelimiter(tokenizer)) {
            space(tokenizer)
            tokenizer.consume()
            space(tokenizer)

            arguments.add(
                argument.parse(tokenizer)
            )
        }

        space(tokenizer)
        return factory.createNode(arguments)
    }

    protected fun function(tokenizer: PublicApi.ParserEngine): PublicApi.Node {
        tokenizer.consume()
        space(tokenizer)

        val functionId = identifier(tokenizer)

        space(tokenizer)

        val function = if (tokenizer.currentToken.isFunctionArgumentIndicator()) {
            FunctionNode(
                functionId,
                arguments(functionId, tokenizer)
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
