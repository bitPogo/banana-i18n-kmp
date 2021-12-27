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
    var parse: ((tokenizer: BananaContract.TokenStore) -> BananaContract.Node)? = null
) : BananaContract.ParserPlugin, MockContract.Mock {
    override fun parse(tokenizer: BananaContract.TokenStore): BananaContract.Node {
        return parse?.invoke(tokenizer)
            ?: throw MockError.MissingStub("Missing sideeffect for parse")
    }

    override fun clear() {
        parse = null
    }
}
