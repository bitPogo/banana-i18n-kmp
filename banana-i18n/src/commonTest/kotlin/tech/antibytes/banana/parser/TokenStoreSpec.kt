/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Companion.EOF
import tech.antibytes.banana.PublicApi
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

        store fulfils PublicApi.TokenStore::class
    }

    @Test
    fun `It fulfils TokenStoreResetter`() {
        val store: Any = TokenStore(tokenizer.also { it.next = { EOF } })

        store fulfils BananaContract.TokenStoreResetter::class
    }

    @Test
    fun `It points to the current Token and its first follower`() {
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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

    @Test
    fun `Given lookahead is called with an Int, it returns EOF if no Token is available on the given position`() {
        // Given
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
        val token = store.lookahead(5)

        // Then
        token mustBe EOF
    }

    @Test
    fun `Given lookahead is called with an Int, it returns the Token if available on the given position`() {
        // Given
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
        val token = store.lookahead(2)

        // Then
        token mustBe tokens[2]
    }

    @Test
    fun `Given lookahead is called with an Int, it returns the Token if available on the given position, while using the TokenBuffer`() {
        // Given
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
        store.lookahead(3)
        val token = store.lookahead(2)

        // Then
        token mustBe tokens[2]
    }

    @Test
    fun `Given lookahead is called with an Int, it returns the Token if available on the given position, while extending the TokenBuffer`() {
        // Given
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
        store.lookahead(3)
        val token = store.lookahead(4)

        // Then
        token mustBe tokens[4]
    }

    @Test
    fun `Given lookahead is called with an Int which is zero, it returns current Token`() {
        // Given
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
        val token = store.lookahead(0)

        // Then
        token mustBe store.currentToken
    }

    @Test
    fun `Given lookahead is called with an Int which is negative, it returns current Token`() {
        // Given
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
        val token = store.lookahead(-23)

        // Then
        token mustBe store.currentToken
    }

    @Test
    fun `Given lookahead is called with an Int which is one, it returns lookahead Token`() {
        // Given
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
        val token = store.lookahead(1)

        // Then
        token mustBe store.lookahead
    }

    @Test
    fun `Given is shift called, it reads from the token buffer until it is cleared`() {
        // Given
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
        store.lookahead(2)
        store.shift()
        store.shift()

        store.shift()
        store.shift()
        val buffer = store.resolveValues()

        // Then
        buffer[0] mustBe tokens[0].value
        buffer[1] mustBe tokens[1].value
        buffer[2] mustBe tokens[2].value
        buffer[3] mustBe tokens[3].value
    }

    @Test
    fun `Given consume is called, it reads from the token buffer until it is cleared`() {
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
        store.lookahead(2)
        store.consume()
        store.consume()
        store.consume()

        // Then
        store.currentToken mustBe tokens[3]
        store.lookahead mustBe tokens[4]
    }

    @Test
    fun `Given reset is called, it resets the storage to current and lookahead `() {
        val tokens = listOf(
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
                fixture<Int>().toString(),
                -1,
                -1,
            ),
            PublicApi.Token(
                PublicApi.TokenTypes.INTEGER,
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
        store.reset()

        // Then
        store.currentToken mustBe tokens[3]
        store.lookahead mustBe tokens[4]
        store.resolveValues().isEmpty() mustBe true
    }
}
