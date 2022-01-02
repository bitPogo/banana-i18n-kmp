/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode.CompoundNode

internal class DefaultArgumentsParser private constructor(
    logger: PublicApi.Logger,
    parserPluginController: PublicApi.ParserPluginController
) : PublicApi.ParserPlugin, SharedParserRules(logger, parserPluginController) {
    private fun argument(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        val argument = mutableListOf<PublicApi.Node>()

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

    override fun parse(tokenizer: PublicApi.TokenStore): PublicApi.Node = argument(tokenizer)

    companion object : PublicApi.ParserPluginFactory {
        override fun createPlugin(
            logger: PublicApi.Logger,
            plugins: PublicApi.ParserPluginController
        ): PublicApi.ParserPlugin = DefaultArgumentsParser(logger, plugins)
    }
}
