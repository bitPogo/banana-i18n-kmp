/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Companion.EOF
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class TokenStoreFake(
    private var _tokens: MutableList<BananaContract.Token> = mutableListOf(EOF, EOF),
    var tokenizerStub: BananaContract.Tokenizer? = null
) : BananaContract.TokenStore, MockContract.Mock {
    val capturedShiftedTokens = mutableListOf<BananaContract.Token>()
    private val stringBuffer: MutableList<String> = mutableListOf()

    var tokens: MutableList<BananaContract.Token>
        get() = _tokens
        set(newValues) {
            _tokens = newValues
            _currentToken = tokens.removeFirstOrNull() ?: EOF
            _lookahead = tokens.removeFirstOrNull() ?: EOF
        }

    override val currentToken: BananaContract.Token
        get() = _currentToken
    override val lookahead: BananaContract.Token
        get() = _lookahead

    override val tokenizer: BananaContract.Tokenizer
        get() = tokenizerStub ?: throw MockError.MissingStub("Missing property tokenizerStub.")

    private var _currentToken: BananaContract.Token = tokens.removeAt(0)
    private var _lookahead: BananaContract.Token = tokens.removeAt(0)

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

    override fun lookahead(k: Int): BananaContract.Token {
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
