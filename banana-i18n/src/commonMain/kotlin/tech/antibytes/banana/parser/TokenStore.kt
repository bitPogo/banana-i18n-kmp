/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Companion.EOF

internal class TokenStore(
    override val tokenizer: BananaContract.Tokenizer
) : BananaContract.TokenStore {
    override val currentToken: BananaContract.Token
        get() = _currentToken
    override val lookahead: BananaContract.Token
        get() = tokenBuffer[0]

    private var _currentToken: BananaContract.Token = tokenizer.next()
    private val tokenBuffer = mutableListOf(tokenizer.next())
    private var stringBuffer: MutableList<String> = mutableListOf()

    private fun next() {
        _currentToken = tokenBuffer.removeAt(0)
        lookahead(1)
    }

    override fun shift() {
        stringBuffer.add(_currentToken.value)
        next()
    }

    override fun consume() = next()

    override fun resolveValues(): List<String> {
        val buffer = stringBuffer.toList()
        stringBuffer.clear()
        return buffer
    }

    private fun fillTokenBuffer(k: Int) {
        if (k > tokenBuffer.size) {
            for (idx in tokenBuffer.lastIndex until k - 1) {
                tokenBuffer.add(tokenizer.next())

                if (tokenBuffer.last() == EOF) {
                    break
                }
            }
        }
    }

    override fun lookahead(k: Int): BananaContract.Token {
        val idx = k - 1
        fillTokenBuffer(k)

        return when {
            idx > tokenBuffer.lastIndex -> EOF
            k <= 0 -> _currentToken
            else -> tokenBuffer[idx]
        }
    }
}
