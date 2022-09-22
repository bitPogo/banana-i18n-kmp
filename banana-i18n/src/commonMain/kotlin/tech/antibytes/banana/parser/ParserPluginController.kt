/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.ParserPluginMap
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.PublicApi.NodeFactory
import tech.antibytes.banana.PublicApi.ParserPlugin
import tech.antibytes.banana.PublicApi.ParserPluginFactory

internal class ParserPluginController(
    logger: PublicApi.Logger,
    defaultPlugin: Pair<ParserPluginFactory, NodeFactory>,
    registeredPlugins: ParserPluginMap = emptyMap(),
) : PublicApi.ParserPluginController {
    private val plugins: Map<String, Pair<ParserPlugin, NodeFactory>> = initializePlugins(
        logger,
        defaultPlugin,
        registeredPlugins,
        this,
    )

    override fun resolvePlugin(name: String): Pair<ParserPlugin, NodeFactory> = plugins.getValue(name)

    private companion object {
        fun initializePlugin(
            logger: PublicApi.Logger,
            plugin: Pair<ParserPluginFactory, NodeFactory>,
            controller: PublicApi.ParserPluginController,
        ): Pair<ParserPlugin, NodeFactory> {
            return Pair(
                plugin.first.createPlugin(logger, controller),
                plugin.second,
            )
        }

        fun initializePlugins(
            logger: PublicApi.Logger,
            defaultPlugin: Pair<ParserPluginFactory, NodeFactory>,
            registeredPlugins: ParserPluginMap,
            controller: PublicApi.ParserPluginController,
        ): Map<String, Pair<ParserPlugin, NodeFactory>> {
            val initializedPlugins: MutableMap<String, Pair<ParserPlugin, NodeFactory>> = mutableMapOf()
            val default = initializePlugin(logger, defaultPlugin, controller)

            registeredPlugins.forEach { (name, plugin) ->
                initializedPlugins[name] = initializePlugin(logger, plugin, controller)
            }

            return initializedPlugins.withDefault { default }
        }
    }
}
