/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.TokenTypes
import tech.antibytes.banana.BananaContract.Companion.EOF
import tech.antibytes.banana.ast.CompoundNode
import tech.antibytes.banana.ast.MagicLinkNode
import tech.antibytes.banana.ast.MagicWordNode
import tech.antibytes.banana.ast.TextNode
import tech.antibytes.banana.ast.VariableNode
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.createTokens
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test


class TopLevelParserSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = TokenStoreFake()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
    }

    @Test
    fun `It fulfils TopLevelParser`() {
        val parser: Any = TopLevelParser()

        parser fulfils BananaContract.TopLevelParser::class
    }

    @Test
    fun `Given parse is called it accepts Empty Messages`() {
        // Given
        val parser = TopLevelParser()
        tokenStore.tokens.addAll(listOf(EOF, EOF))

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts DOUBLE as Text`() {
        // Given
        val parser = TopLevelParser()
        val double = fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                TokenTypes.DOUBLE to double,
            )
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
        val parser = TopLevelParser()
        val integer = fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                TokenTypes.INTEGER to integer,
            )
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
        val parser = TopLevelParser()
        val escaped = "\$"

        val tokens = createTokens(
            listOf(
                TokenTypes.ESCAPED to escaped,
            )
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
        val parser = TopLevelParser()
        val ascii = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val tokens = createTokens(
            listOf(
                TokenTypes.ASCII_STRING to ascii,
            )
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
        val parser = TopLevelParser()
        val nonAscii = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                TokenTypes.NON_ASCII_STRING to nonAscii,
            )
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
        val parser = TopLevelParser()
        val literal = "%"

        val tokens = createTokens(
            listOf(
                TokenTypes.LITERAL to literal,
            )
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
        val parser = TopLevelParser()
        val space = " "

        val tokens = createTokens(
            listOf(
                TokenTypes.WHITESPACE to space,
            )
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
        val parser = TopLevelParser()
        val delimiter = "|"

        val tokens = createTokens(
            listOf(
                TokenTypes.DELIMITER to delimiter,
            )
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
    fun `Given parse is called it accepts FUNCTION_END as Text`() {
        // Given
        val parser = TopLevelParser()
        val end = "}}"

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_END to end,
            )
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
    fun `Given parse is called it accepts LINK_END as Text`() {
        // Given
        val parser = TopLevelParser()
        val end = "]]"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_END to end,
            )
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
    fun `Given parse is called it accepts multiple specified Tokens as one Text`() {
        // Given
        val parser = TopLevelParser()
        val ascii = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        val space = " "
        val nonAscii = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                TokenTypes.ASCII_STRING to ascii,
                TokenTypes.WHITESPACE to space,
                TokenTypes.NON_ASCII_STRING to nonAscii,
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf(ascii, space, nonAscii))
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0], tokens[1], tokens[2])
    }

    @Test
    fun `Given parse is called it accepts VARIABLE as Variable`() {
        // Given
        val parser = TopLevelParser()
        val variable = "1"

        val tokens = createTokens(
            listOf(
                TokenTypes.VARIABLE to variable,
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe VariableNode(variable)
        tokenStore.capturedShiftedTokens mustBe emptyList<BananaContract.Token>()
    }

    @Test
    fun `Given parse is called it accepts MagicWords`() {
        // Given
        val parser = TopLevelParser()
        val word = "WORD"

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.ASCII_STRING to word,
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe MagicWordNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts MagicWords with Identifiers`() {
        // Given
        val parser = TopLevelParser()
        val word1 = "WORD1"
        val word2 = "WORD2"

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.ASCII_STRING to word1,
                TokenTypes.LITERAL to "_",
                TokenTypes.ASCII_STRING to word2,
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe MagicWordNode("${word1}_$word2")
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1], tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts MagicWords which contain additional spaces`() {
        // Given
        val parser = TopLevelParser()
        val word = "WORD"

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.ASCII_STRING to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe MagicWordNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Function like syntax as Text`() {
        // Given
        val parser = TopLevelParser()
        val word = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.NON_ASCII_STRING to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(
            listOf(tokens[0].value, tokens[1].value, tokens[2].value, tokens[3].value, tokens[4].value)
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts MagicLinks`() {
        // Given
        val parser = TopLevelParser()
        val word = "WORD"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.ASCII_STRING to word,
                TokenTypes.FUNCTION_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe MagicLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts MagicLinks with Identifiers`() {
        // Given
        val parser = TopLevelParser()
        val word1 = "WORD1"
        val word2 = "WORD2"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.ASCII_STRING to word1,
                TokenTypes.LITERAL to "_",
                TokenTypes.ASCII_STRING to word2,
                TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe MagicLinkNode("${word1}_$word2")
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1], tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Links which contain additional spaces`() {
        // Given
        val parser = TopLevelParser()
        val word = "WORD"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.ASCII_STRING to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe MagicLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Link like syntax as Text`() {
        // Given
        val parser = TopLevelParser()
        val word = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.NON_ASCII_STRING to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(
            listOf(tokens[0].value, tokens[1].value, tokens[2].value, tokens[3].value, tokens[4].value)
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[0], tokens[1], tokens[2], tokens[3], tokens[4])
        tokenStore.tokens.isEmpty() mustBe true
    }
}
