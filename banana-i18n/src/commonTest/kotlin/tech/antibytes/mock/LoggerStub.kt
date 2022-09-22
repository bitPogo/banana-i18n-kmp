/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock

import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.MockContract

internal class LoggerStub : MockContract.Mock, PublicApi.Logger {
    val warning: MutableList<Pair<PublicApi.Tag, String>> = mutableListOf()
    val error: MutableList<Pair<PublicApi.Tag, String>> = mutableListOf()

    override fun warning(tag: PublicApi.Tag, message: String) {
        warning.add(tag to message)
    }

    override fun error(tag: PublicApi.Tag, message: String) {
        error.add(tag to message)
    }

    override fun clear() {
        warning.clear()
        error.clear()
    }
}
