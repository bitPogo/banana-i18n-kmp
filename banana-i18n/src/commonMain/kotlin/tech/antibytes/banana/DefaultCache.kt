/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

internal class DefaultCache<T: Any> : PublicApi.Cache<T> {
    override fun getValue(key: String): T? = null

    override fun store(key: String, value: T) = Unit
}
