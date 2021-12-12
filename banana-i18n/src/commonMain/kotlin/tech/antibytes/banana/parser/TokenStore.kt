/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Companion.EOF

// TODO: Investigate if LL(k) has sense to use as for now LL(1) should be sufficient
internal class TokenStore(
    override val tokenizer: BananaContract.Tokenizer
) : BananaContract.TokenStore {
    override val currentToken: BananaContract.Token
        get() = _currentToken
    override val lookahead: BananaContract.Token
        get() = _lookahead

    private var _currentToken: BananaContract.Token = tokenizer.next()
    private var _lookahead: BananaContract.Token = tokenizer.next()
    private var stringBuffer: MutableList<String> = mutableListOf()

    private fun nextToken() {
        _currentToken = _lookahead
        _lookahead = tokenizer.next()
    }

    override fun shift() {
        stringBuffer.add(_currentToken.value)
        nextToken()
    }

    override fun resolveValues(): Array<String> = stringBuffer.toTypedArray()

    override fun consume() = nextToken()

    override fun hasNext(): Boolean = _lookahead != EOF

    override fun next(): BananaContract.Token {
        nextToken()

        return _currentToken
    }
}
