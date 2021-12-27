/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.mock.parser.NodeFactoryStub
import tech.antibytes.mock.parser.ParserPluginStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

class ParserPluginControllerSpec {
    private val logger = LoggerStub()

    @Test
    fun `It fulfils ParserPluginController`() {
        ParserPluginController(
            logger,
            Pair(
                ParserPluginStub,
                NodeFactoryStub()
            )
        ) fulfils BananaContract.ParserPluginController::class
    }

    @Test
    fun `Given resolvePlugin is called with a String, it resolves the given DefaultParser and its NodeFactory if the name matches no Plugin`() {
        // Given
        val factory = NodeFactoryStub()
        val default = Pair(
            ParserPluginStub,
            factory
        )
        val controller = ParserPluginController(logger, default)

        // When
        val plugin = controller.resolvePlugin("plugin")

        // Then
        plugin.first sameAs ParserPluginStub.lastInstance
        plugin.second sameAs factory
    }

    @Test
    fun `Given resolvePlugin is called with a String, it resolves a custom ParserPlugin and its NodeFactory if the name matches no Plugin`() {
        // Given
        val pluginId = "name"
        val customPlugin = Pair(ParserPluginStub(), NodeFactoryStub())
        val customPlugins = mapOf(
            pluginId to customPlugin
        )

        val controller = ParserPluginController(
            logger,
            defaultPlugin = Pair(
                ParserPluginStub,
                NodeFactoryStub()
            ),
            registeredPlugins = customPlugins
        )

        // When
        val plugin = controller.resolvePlugin(pluginId)

        // Then
        plugin sameAs customPlugin
    }
}
