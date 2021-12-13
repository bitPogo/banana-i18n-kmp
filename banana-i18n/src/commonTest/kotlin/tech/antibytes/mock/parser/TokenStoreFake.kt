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
            _tokens = newValues.also {
                if (it.size < 2) {
                    throw RuntimeException("You must at least specify 2 tokens.")
                }
            }
            _currentToken = tokens.removeAt(0)
            _lookahead = tokens.removeAt(0)
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
        _lookahead = tokens.removeAt(0)
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

    override fun clear() {
        tokens = mutableListOf(EOF, EOF)
        stringBuffer.clear()
        tokenizerStub = null
        capturedShiftedTokens.clear()
    }
}
