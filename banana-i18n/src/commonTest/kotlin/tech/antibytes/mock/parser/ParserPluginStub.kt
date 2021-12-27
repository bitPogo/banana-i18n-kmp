/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class ParserPluginStub(
    private val parse: ((tokenizer: PublicApi.TokenStore) -> PublicApi.Node)? = null
) : PublicApi.ParserPlugin {
    override fun parse(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        return parse?.invoke(tokenizer)
            ?: throw MockError.MissingStub("Missing sideeffect for parse")
    }

    companion object : PublicApi.ParserPluginFactory, MockContract.Mock {
        var parse: ((tokenizer: PublicApi.TokenStore) -> PublicApi.Node)? = null
        val lastInstance: PublicApi.ParserPlugin
            get() = capturedInstance
        lateinit var capturedInstance: PublicApi.ParserPlugin

        override fun createPlugin(
            logger: PublicApi.Logger,
            plugins: PublicApi.ParserPluginController
        ): PublicApi.ParserPlugin = ParserPluginStub(parse).also { capturedInstance = it }

        override fun clear() {
            parse = null
        }
    }
}
