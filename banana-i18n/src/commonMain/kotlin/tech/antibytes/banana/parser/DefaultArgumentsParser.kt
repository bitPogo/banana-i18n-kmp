/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNodes.CompoundNode

internal class DefaultArgumentsParser private constructor(
    logger: BananaContract.Logger,
    parserPluginController: BananaContract.ParserPluginController
) : BananaContract.ParserPlugin, SharedParserRules(logger, parserPluginController) {
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

    override fun parse(tokenizer: BananaContract.TokenStore): BananaContract.Node = argument(tokenizer)

    companion object : BananaContract.ParserPluginFactory {
        override fun createPlugin(
            logger: BananaContract.Logger,
            plugins: BananaContract.ParserPluginController
        ): BananaContract.ParserPlugin = DefaultArgumentsParser(logger, plugins)
    }
}
