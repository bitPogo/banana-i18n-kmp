/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.MockContract

internal class LoggerStub: MockContract.Mock, BananaContract.Logger {
    val info: MutableList<Pair<BananaContract.Tag, String>> = mutableListOf()
    val warning: MutableList<Pair<BananaContract.Tag, String>> = mutableListOf()
    val error: MutableList<Pair<BananaContract.Tag, String>> = mutableListOf()

    override fun info(tag: BananaContract.Tag, message: String) {
        info.add(tag to message)
    }

    override fun warning(tag: BananaContract.Tag, message: String) {
        warning.add(tag to message)
    }

    override fun error(tag: BananaContract.Tag, message: String) {
        error.add(tag to message)
    }

    override fun clear() {
        info.clear()
        warning.clear()
        error.clear()
    }
}
