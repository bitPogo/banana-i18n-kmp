/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.util

import tech.antibytes.banana.BananaContract

internal fun createTokens(
    templates: List<Pair<BananaContract.TokenTypes, String>>
): MutableList<BananaContract.Token> {
    var column = 0
    var line = 0

    val tokens = mutableListOf<BananaContract.Token>()

    for (template in templates) {
        val (type, value) = template

        tokens.add(
            BananaContract.Token(
                type,
                value,
                column,
                line
            )
        )

        if (type == BananaContract.TokenTypes.WHITESPACE && value.contains("\n")) {
            line++
            column = 0
        } else {
            column += value.length
        }
    }

    return tokens
}
