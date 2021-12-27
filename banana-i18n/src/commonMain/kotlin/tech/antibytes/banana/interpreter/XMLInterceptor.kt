/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.PublicApi

class XMLInterceptor : PublicApi.TextInterceptor {
    override fun intercept(chunk: String): String {
        return when (chunk) {
            "<" -> "&lt;"
            ">" -> "&gt;"
            "&" -> "&amp;"
            "'" -> "&#39;"
            "\"" -> "&quot;"
            else -> chunk
        }
    }
}
