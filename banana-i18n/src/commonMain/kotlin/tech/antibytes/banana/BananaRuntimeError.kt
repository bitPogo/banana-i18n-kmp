/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

open class BananaRuntimeError(
    override val message: String,
    override val cause: Throwable? = null
) : RuntimeException()
