/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock

import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class CacheStub<T: Any>(
    var getValue: ((String) -> T?)? = null,
    var store: ((String, value: T) -> Unit)? = null
) : PublicApi.Cache<T>, MockContract.Mock {
    override fun getValue(key: String): T? {
        return if (getValue == null) {
            throw MockError.MissingStub("Missing sideeffect for getValue!")
        } else {
            getValue!!.invoke(key)
        }
    }

    override fun store(key: String, value: T) {
        return store?.invoke(key, value) ?: throw MockError.MissingStub("Missing sideeffect for store!")
    }

    override fun clear() {
        getValue = null
        store = null
    }
}
