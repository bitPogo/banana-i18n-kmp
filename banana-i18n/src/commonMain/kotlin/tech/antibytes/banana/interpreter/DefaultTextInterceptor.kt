/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.PublicApi

class DefaultTextInterceptor : PublicApi.TextInterceptor {
    override fun intercept(chunk: String): String = chunk
}
