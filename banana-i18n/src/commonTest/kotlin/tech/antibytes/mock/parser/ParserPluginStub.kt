/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class ParserPluginStub(
    private val parse: ((tokenizer: BananaContract.TokenStore) -> BananaContract.Node)? = null
) : BananaContract.ParserPlugin {
    override fun parse(tokenizer: BananaContract.TokenStore): BananaContract.Node {
        return parse?.invoke(tokenizer)
            ?: throw MockError.MissingStub("Missing sideeffect for parse")
    }

    companion object : BananaContract.ParserPluginFactory, MockContract.Mock {
        var parse: ((tokenizer: BananaContract.TokenStore) -> BananaContract.Node)? = null
        val lastInstance: BananaContract.ParserPlugin
            get() = capturedInstance
        lateinit var capturedInstance: BananaContract.ParserPlugin

        override fun createPlugin(
            logger: BananaContract.Logger,
            plugins: BananaContract.ParserPluginController
        ): BananaContract.ParserPlugin = ParserPluginStub(parse).also { capturedInstance = it }

        override fun clear() {
            parse = null
        }
    }
}
