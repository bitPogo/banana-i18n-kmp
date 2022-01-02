/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.MockError

internal class BananaParserStub(
    var parse: ((PublicApi.TokenStore) -> PublicApi.Node)? = null
): BananaContract.TopLevelParser {
    override fun parse(
        tokenizer: PublicApi.TokenStore
    ): PublicApi.Node {
        return parse?.invoke(tokenizer) ?: throw MockError.MissingStub("Missing sideeffect for parse!")
    }
}
