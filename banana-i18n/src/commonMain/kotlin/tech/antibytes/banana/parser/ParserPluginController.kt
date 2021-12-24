/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract

internal class ParserPluginController : BananaContract.ParserPluginController {
    override fun resolvePlugin(name: String): Pair<BananaContract.ParserPlugin, BananaContract.ArgumentsNodeFactory> {
        TODO("Not yet implemented")
    }
}
