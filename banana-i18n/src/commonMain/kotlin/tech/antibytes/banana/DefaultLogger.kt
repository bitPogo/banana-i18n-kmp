/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

internal class DefaultLogger : PublicApi.Logger {
    override fun warning(tag: PublicApi.Tag, message: String) = Unit
    override fun error(tag: PublicApi.Tag, message: String) = Unit
}
