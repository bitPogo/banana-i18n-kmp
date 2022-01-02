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
    private val parse: ((tokenizer: PublicApi.ParserEngine) -> PublicApi.Node)? = null
) : PublicApi.ParserPlugin {
    override fun parse(tokenizer: PublicApi.ParserEngine): PublicApi.Node {
        return parse?.invoke(tokenizer)
            ?: throw MockError.MissingStub("Missing sideeffect for parse")
    }
}

internal class ParserPluginFactoryStub(
    var parse: ((tokenizer: PublicApi.ParserEngine) -> PublicApi.Node)? = null
) : PublicApi.ParserPluginFactory, MockContract.Mock {
    val lastInstances: List<PublicApi.ParserPlugin>
        get() = capturedInstance
    private val capturedInstance: MutableList<PublicApi.ParserPlugin> = mutableListOf()

    override fun createPlugin(
        logger: PublicApi.Logger,
        plugins: PublicApi.ParserPluginController
    ): PublicApi.ParserPlugin = ParserPluginStub(parse).also { capturedInstance.add(it) }

    override fun clear() {
        parse = null
    }
}
