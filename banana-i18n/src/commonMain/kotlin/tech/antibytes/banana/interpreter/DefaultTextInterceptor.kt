/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract

class DefaultTextInterceptor : BananaContract.TextInterceptor {
    override fun intercept(chunk: String): String = chunk
}
