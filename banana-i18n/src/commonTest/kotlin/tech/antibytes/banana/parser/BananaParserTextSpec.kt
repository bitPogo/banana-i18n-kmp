/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import kotlin.test.AfterTest
import kotlin.test.Test
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode.CompoundNode
import tech.antibytes.banana.ast.CoreNode.TextNode
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.parser.ParserPluginControllerStub
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.createTokens
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class BananaParserTextSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = TokenStoreFake()
    private val logger = LoggerStub()
    private val pluginController = ParserPluginControllerStub()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
    }

    @Test
    fun `Given parse is called it accepts DOUBLE as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val double = fixture.fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.DOUBLE to double,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(double))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts INTEGER as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val integer = fixture.fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.INTEGER to integer,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(integer))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts ESCAPED as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val escaped = "\\$"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ESCAPED to escaped,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(escaped))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts ASCII as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val ascii = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to ascii,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(ascii))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts NON_ASCII as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val nonAscii = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.NON_ASCII_STRING to nonAscii,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(nonAscii))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts LITERAL as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val literal = "%"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to literal,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(literal))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts WHITESPACE as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val space = " "

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.WHITESPACE to space,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(space))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts DELIMITER as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val delimiter = "|"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.DELIMITER to delimiter,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(delimiter))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts FUNCTION_START as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val start = "{{"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.FUNCTION_START to start,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(start))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts FUNCTION_END as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val end = "}}"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.FUNCTION_END to end,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(end))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts LINK_START as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val start = "[["

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to start,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(start))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts LINK_END as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val end = "]]"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_END to end,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(end))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts URL as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.URL to url,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(url))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0])
    }

    @Test
    fun `Given parse is called it accepts multiple specified Tokens as one Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val ascii = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val space = " "
        val nonAscii = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to ascii,
                PublicApi.TokenTypes.WHITESPACE to space,
                PublicApi.TokenTypes.NON_ASCII_STRING to nonAscii,
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(ascii, space, nonAscii))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0], tokens[1], tokens[2])
    }
}
