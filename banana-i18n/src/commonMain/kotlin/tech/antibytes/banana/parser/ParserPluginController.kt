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
    logger: BananaContract.Logger,
    defaultPlugin: Pair<BananaContract.ParserPluginFactory, NodeFactory>,
    registeredPlugins: Map<String, Pair<ParserPlugin, NodeFactory>> = emptyMap()
) : BananaContract.ParserPluginController {
    private val plugins = registeredPlugins.withDefault {
        Pair(
            defaultPlugin.first.createPlugin(logger, this),
            defaultPlugin.second
        )
    }

    override fun resolvePlugin(name: String): Pair<ParserPlugin, NodeFactory> = plugins.getValue(name)
}
