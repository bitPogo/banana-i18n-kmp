/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.mock.parser.NodeFactoryStub
import tech.antibytes.mock.parser.ParserPluginStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

class ParserPluginControllerSpec {
    @Test
    fun `It fulfils ParserPluginController`() {
        ParserPluginController(
            Pair(ParserPluginStub(), NodeFactoryStub())
        ) fulfils BananaContract.ParserPluginController::class
    }

    @Test
    fun `Given resolvePlugin is called with a String, it resolves the given DefaultParser and its NodeFactory if the name matches no Plugin`() {
        // Given
        val default = Pair(ParserPluginStub(), NodeFactoryStub())
        val controller = ParserPluginController(default)

        // When
        val plugin = controller.resolvePlugin("plugin")

        // Then
        plugin sameAs default
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
            defaultParserPlugin = Pair(ParserPluginStub(), NodeFactoryStub()),
            customPlugins = customPlugins
        )

        // When
        val plugin = controller.resolvePlugin(pluginId)

        // Then
        plugin sameAs customPlugin
    }
}
