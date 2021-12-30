/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.PublicApi.NodeFactory
import tech.antibytes.banana.PublicApi.ParserPlugin

internal class ParserPluginController(
    logger: PublicApi.Logger,
    defaultPlugin: Pair<PublicApi.ParserPluginFactory, NodeFactory>,
    registeredPlugins: Map<String, Pair<ParserPlugin, NodeFactory>> = emptyMap()
) : PublicApi.ParserPluginController {
    private val plugins = registeredPlugins.withDefault {
        Pair(
            defaultPlugin.first.createPlugin(logger, this),
            defaultPlugin.second
        )
    }

    override fun resolvePlugin(name: String): Pair<ParserPlugin, NodeFactory> = plugins.getValue(name)
}
