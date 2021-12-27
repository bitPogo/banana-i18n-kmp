/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.PublicApi.NodeFactory
import tech.antibytes.banana.PublicApi.ParserPlugin
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class ParserPluginControllerStub(
    var resolvePlugin: ((String) -> Pair<ParserPlugin, NodeFactory>)? = null
) : PublicApi.ParserPluginController, MockContract.Mock {
    override fun resolvePlugin(name: String): Pair<ParserPlugin, NodeFactory> {
        return resolvePlugin?.invoke(name)
            ?: throw MockError.MissingStub("Missing sideeffect for resolvePlugin")
    }

    override fun clear() {
        resolvePlugin = null
    }
}
