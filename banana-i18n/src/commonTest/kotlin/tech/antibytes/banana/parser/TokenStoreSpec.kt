/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Companion.EOF
import tech.antibytes.mock.parser.TokenizerStub
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class TokenStoreSpec {
    private val fixture = kotlinFixture()
    private val tokenizer = TokenizerStub()

    @AfterTest
    fun tearDown() {
        tokenizer.clear()
    }

    @Test
    fun `It fulfils TokenStore`() {
        val store: Any = TokenStore(tokenizer.also { it.next = { EOF } })

        store fulfils BananaContract.TokenStore::class
    }

    @Test
    fun `It points to the current Token and its first follower`() {
        val tokens = listOf(
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            )
        )
        val consumableTokens = tokens.toMutableList()

        tokenizer.next = { consumableTokens.removeAt(0) }

        val store = TokenStore(tokenizer)

        store.currentToken mustBe tokens[0]
        store.lookahead mustBe tokens[1]
    }

    @Test
    fun `Given consume is called, it sets a new lookahead and sets the old lookahead as currentToken`() {
        // Given
        val tokens = listOf(
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            )
        )
        val consumableTokens = tokens.toMutableList()

        tokenizer.next = { consumableTokens.removeAt(0) }

        val store = TokenStore(tokenizer)

        // When
        val result = store.consume()

        // Then
        result mustBe Unit
        store.currentToken mustBe tokens[1]
        store.lookahead mustBe tokens[2]
    }

    @Test
    fun `Given shift is called, it sets a new lookahead and sets the old lookahead as currentToken`() {
        // Given
        val tokens = listOf(
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            )
        )
        val consumableTokens = tokens.toMutableList()

        tokenizer.next = { consumableTokens.removeAt(0) }

        val store = TokenStore(tokenizer)

        // When
        val result = store.shift()

        // Then
        result mustBe Unit
        store.currentToken mustBe tokens[1]
        store.lookahead mustBe tokens[2]
    }

    @Test
    fun `Given resolveValues is called, it returns the current string buffer`() {
        // Given
        tokenizer.next = { EOF }

        val store = TokenStore(tokenizer)

        // When
        val buffer = store.resolveValues()

        // Then
        buffer.isEmpty() mustBe true
    }

    @Test
    fun `Given shift is called and resolveValues afterwards, it shifts the values of the currentTokens returns the shifted values`() {
        // Given
        val tokens = listOf(
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            EOF
        )
        val consumableTokens = tokens.toMutableList()

        tokenizer.next = { consumableTokens.removeAt(0) }

        val store = TokenStore(tokenizer)

        // When
        store.shift()
        store.shift()
        val buffer = store.resolveValues()

        // Then
        buffer[0] mustBe tokens[0].value
        buffer[1] mustBe tokens[1].value
    }

    @Test
    fun `Given shift is called and resolveValues afterwards, it clears the buffer on resolveValues`() {
        // Given
        val tokens = listOf(
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            BananaContract.Token(
                BananaContract.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            EOF
        )
        val consumableTokens = tokens.toMutableList()

        tokenizer.next = { consumableTokens.removeAt(0) }

        val store = TokenStore(tokenizer)

        // When
        store.shift()
        store.shift()
        store.resolveValues()

        store.shift()
        store.shift()
        val buffer = store.resolveValues()

        // Then
        buffer[0] mustBe tokens[2].value
        buffer[1] mustBe tokens[3].value
    }
}
