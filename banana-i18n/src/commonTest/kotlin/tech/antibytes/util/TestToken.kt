/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.util

import tech.antibytes.banana.PublicApi

internal fun createTokens(
    templates: List<Pair<PublicApi.TokenTypes, String>>
): MutableList<PublicApi.Token> {
    var column = 0
    var line = 0

    val tokens = mutableListOf<PublicApi.Token>()

    for (template in templates) {
        val (type, value) = template

        tokens.add(
            PublicApi.Token(
                type,
                value,
                column,
                line
            )
        )

        if (type == PublicApi.TokenTypes.WHITESPACE && value.contains("\n")) {
            line++
            column = 0
        } else {
            column += value.length
        }
    }

    return tokens
}
