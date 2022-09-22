/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import kotlin.test.Test
import tech.antibytes.banana.PublicApi
import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.parser.NodeFactoryStub
import tech.antibytes.mock.parser.ParserPluginFactoryStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.sameAs

class ParserPluginControllerSpec {
    private val logger = LoggerStub()

    @Test
    fun `It fulfils ParserPluginController`() {
        ParserPluginController(
            logger,
            Pair(
                ParserPluginFactoryStub(),
                NodeFactoryStub(),
            ),
        ) fulfils PublicApi.ParserPluginController::class
    }

    @Test
    fun `Given resolvePlugin is called with a String, it resolves the given DefaultParser and its NodeFactory if the name matches no Plugin`() {
        // Given
        val nodeFactory = NodeFactoryStub()
        val parserPluginFactory = ParserPluginFactoryStub()
        val default = Pair(
            parserPluginFactory,
            nodeFactory,
        )
        val controller = ParserPluginController(logger, default)

        // When
        val plugin = controller.resolvePlugin("plugin")

        // Then
        plugin.first sameAs parserPluginFactory.lastInstances.first()
        plugin.second sameAs nodeFactory
    }

    @Test
    fun `Given resolvePlugin is called with a String, it resolves a custom ParserPlugin and its NodeFactory if the name matches no Plugin`() {
        // Given
        val pluginId = "name"
        val customPlugin = Pair(ParserPluginFactoryStub(), NodeFactoryStub())
        val customPlugins = mapOf(
            pluginId to customPlugin,
        )

        val controller = ParserPluginController(
            logger,
            defaultPlugin = Pair(
                ParserPluginFactoryStub(),
                NodeFactoryStub(),
            ),
            registeredPlugins = customPlugins,
        )

        // When
        val plugin = controller.resolvePlugin(pluginId)

        // Then
        plugin.first sameAs customPlugin.first.lastInstances.first()
        plugin.second sameAs customPlugin.second
    }
}
