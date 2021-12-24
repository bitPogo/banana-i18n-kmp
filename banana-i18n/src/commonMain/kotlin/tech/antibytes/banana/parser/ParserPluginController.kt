/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.NodeFactory
import tech.antibytes.banana.BananaContract.ParserPlugin

internal class ParserPluginController(
    defaultParserPlugin: Pair<ParserPlugin, NodeFactory>,
    customPlugins: Map<String, Pair<ParserPlugin, NodeFactory>> = emptyMap()
) : BananaContract.ParserPluginController {
    private val plugins = customPlugins.withDefault { defaultParserPlugin }

    override fun resolvePlugin(name: String): Pair<ParserPlugin, NodeFactory> = plugins.getValue(name)
}
