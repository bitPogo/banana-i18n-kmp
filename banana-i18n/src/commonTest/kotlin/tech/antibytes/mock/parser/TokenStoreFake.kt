/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Companion.EOF
import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.MockContract

internal class TokenStoreFake(
    private var _tokens: MutableList<PublicApi.Token> = mutableListOf(EOF, EOF),
    var tokenizerStub: BananaContract.Tokenizer? = null
) : PublicApi.TokenStore, MockContract.Mock {
    val capturedShiftedTokens = mutableListOf<PublicApi.Token>()
    private val stringBuffer: MutableList<String> = mutableListOf()

    var tokens: MutableList<PublicApi.Token>
        get() = _tokens
        set(newValues) {
            _tokens = newValues
            _currentToken = tokens.removeFirstOrNull() ?: EOF
            _lookahead = tokens.removeFirstOrNull() ?: EOF
        }

    override val currentToken: PublicApi.Token
        get() = _currentToken
    override val lookahead: PublicApi.Token
        get() = _lookahead

    private var _currentToken: PublicApi.Token = tokens.removeAt(0)
    private var _lookahead: PublicApi.Token = tokens.removeAt(0)

    private fun nextToken() {
        _currentToken = _lookahead
        _lookahead = tokens.removeFirstOrNull() ?: EOF
    }

    override fun shift() {
        capturedShiftedTokens.add(_currentToken)
        stringBuffer.add(_currentToken.value)
        nextToken()
    }

    override fun resolveValues(): List<String> {
        val buffer = stringBuffer.toList()
        stringBuffer.clear()
        return buffer
    }

    override fun consume() = nextToken()

    override fun lookahead(k: Int): PublicApi.Token {
        return if (k > 1) {
            tokens.getOrElse(k - 2) { EOF }
        } else {
            _lookahead
        }
    }

    override fun clear() {
        tokens = mutableListOf(EOF, EOF)
        stringBuffer.clear()
        tokenizerStub = null
        capturedShiftedTokens.clear()
    }
}
