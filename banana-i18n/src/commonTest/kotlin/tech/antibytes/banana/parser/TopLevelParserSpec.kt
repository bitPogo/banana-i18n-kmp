/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Tag
import tech.antibytes.banana.BananaContract.TokenTypes
import tech.antibytes.banana.BananaContract.Companion.EOF
import tech.antibytes.banana.ast.CompoundNode
import tech.antibytes.banana.ast.HeadlessFreeLinkNode
import tech.antibytes.banana.ast.HeadlessLinkNode
import tech.antibytes.banana.ast.FunctionNode
import tech.antibytes.banana.ast.TextNode
import tech.antibytes.banana.ast.VariableNode
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.createTokens
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class TopLevelParserSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = TokenStoreFake()
    private val logger = LoggerStub()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
    }

    @Test
    fun `It fulfils TopLevelParser`() {
        val parser: Any = TopLevelParser(logger)

        parser fulfils BananaContract.TopLevelParser::class
    }

    @Test
    fun `Given parse is called it accepts Empty Messages`() {
        // Given
        val parser = TopLevelParser(logger)
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
        val parser = TopLevelParser(logger)
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
        val parser = TopLevelParser(logger)
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
        val parser = TopLevelParser(logger)
        val escaped = "\\$"

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
        val parser = TopLevelParser(logger)
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
        val parser = TopLevelParser(logger)
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
        val parser = TopLevelParser(logger)
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
        val parser = TopLevelParser(logger)
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
        val parser = TopLevelParser(logger)
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
        val parser = TopLevelParser(logger)
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
        val parser = TopLevelParser(logger)
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
    fun `Given parse is called it accepts URL as Text`() {
        // Given
        val parser = TopLevelParser(logger)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                TokenTypes.URL to url,
            )
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
        val parser = TopLevelParser(logger)
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
        val parser = TopLevelParser(logger)
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
    fun `Given parse is called it accepts HeadlessFunctions`() {
        // Given
        val parser = TopLevelParser(logger)
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
        (message as CompoundNode).children[0] mustBe FunctionNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Functions with Identifiers`() {
        // Given
        val parser = TopLevelParser(logger)
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
        (message as CompoundNode).children[0] mustBe FunctionNode("${word1}_$word2")
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1], tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Functions which contain additional spaces`() {
        // Given
        val parser = TopLevelParser(logger)
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
        (message as CompoundNode).children[0] mustBe FunctionNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Functions with ASCII as single Argument as Text which contain additional spacing`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "WORD2"

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.ASCII_STRING to name,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LITERAL to ":",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.ASCII_STRING to argument,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(
            name,
            listOf(
                TextNode(listOf(argument))
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Functions with NON_ASCII as single Argument as Text which contain additional spacing`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.ASCII_STRING to name,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LITERAL to ":",
                TokenTypes.NON_ASCII_STRING to argument,
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(
            name,
            listOf(
                TextNode(listOf(argument))
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Functions with DOUBLE as single Argument as Text which contain additional spacing`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.ASCII_STRING to name,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LITERAL to ":",
                TokenTypes.DOUBLE to argument,
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(
            name,
            listOf(
                TextNode(listOf(argument))
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Functions with INTEGER as single Argument as Text which contain additional spacing`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.ASCII_STRING to name,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LITERAL to ":",
                TokenTypes.INTEGER to argument,
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(
            name,
            listOf(
                TextNode(listOf(argument))
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Functions with ESCAPED as single Argument as Text which contain additional spacing`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "\\{"

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.ASCII_STRING to name,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LITERAL to ":",
                TokenTypes.ESCAPED to argument,
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(
            name,
            listOf(
                TextNode(listOf(argument))
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Functions with LITERAL as single Argument as Text which contain additional spacing`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "{"

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.ASCII_STRING to name,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LITERAL to ":",
                TokenTypes.LITERAL to argument,
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(
            name,
            listOf(
                TextNode(listOf(argument))
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Functions with WHITESPACES as single Argument as Text which contain additional spacing`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "{"
        val space = " "

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.ASCII_STRING to name,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LITERAL to ":",
                TokenTypes.LITERAL to argument,
                TokenTypes.WHITESPACE to space,
                TokenTypes.LITERAL to argument,
                TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(
            name,
            listOf(
                TextNode(listOf(argument, space, argument))
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Functions like syntax as Text`() {
        // Given
        val parser = TopLevelParser(logger)
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
    fun `Given parse is called it accepts Functions while it had not been closed and reports a warning`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "abc"

        val tokens = createTokens(
            listOf(
                TokenTypes.FUNCTION_START to "{{",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.ASCII_STRING to word,
                TokenTypes.WHITESPACE to " ",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning[0] mustBe Pair(
            Tag.PARSER,
            "Warning: Function ($word) had not been closed!"
        )
    }

    @Test
    fun `Given parse is called it accepts Links`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "WORD"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.ASCII_STRING to word,
                TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe HeadlessLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Links with Identifiers`() {
        // Given
        val parser = TopLevelParser(logger)
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
        (message as CompoundNode).children[0] mustBe HeadlessLinkNode("${word1}_$word2")
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1], tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Links which contain additional spaces`() {
        // Given
        val parser = TopLevelParser(logger)
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
        (message as CompoundNode).children[0] mustBe HeadlessLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts DOUBLE as Link`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.DOUBLE to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe HeadlessLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts INTEGER as Link`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.INTEGER to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe HeadlessLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts NON_ASCII as Link`() {
        // Given
        val parser = TopLevelParser(logger)
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
        (message as CompoundNode).children[0] mustBe HeadlessLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts LITERAL as Link`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "!"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LITERAL to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe HeadlessLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts ESCAPED as Link`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "\\{"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.ESCAPED to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe HeadlessLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Link like as Link`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "\\{"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.ESCAPED to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe HeadlessLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts Link while, while it had not been closed, if it is locatated at the End of the Message and reports a warning`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "abc"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.ASCII_STRING to word,
                TokenTypes.WHITESPACE to " ",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe HeadlessLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning[0] mustBe Pair(
            Tag.PARSER,
            "Warning: Link ($word) had not been closed!"
        )
    }

    @Test
    fun `Given parse is called it accepts Link while it encountered an unexpected token and reports an error`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "abc"

        val tokens = createTokens(
            listOf(
                TokenTypes.LINK_START to "[[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.ASCII_STRING to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LITERAL to "[",
                TokenTypes.ASCII_STRING to "not important",
                TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        message as CompoundNode
        message.children[0] mustBe HeadlessLinkNode(word)
        message.children[1] fulfils TextNode::class
        tokenStore.tokens.isEmpty() mustBe true
        logger.error[0] mustBe Pair(
            Tag.PARSER,
            "Error: Unexpected Token (${tokens[4]}) in Link ($word)!"
        )
    }

    @Test
    fun `Given parse is called it accepts FreeLinks`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "https://example.org"

        val tokens = createTokens(
            listOf(
                TokenTypes.LITERAL to "[",
                TokenTypes.URL to word,
                TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe HeadlessFreeLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe emptyList<BananaContract.Token>()
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with additional spacing`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "https://example.org"

        val tokens = createTokens(
            listOf(
                TokenTypes.LITERAL to "[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.URL to word,
                TokenTypes.WHITESPACE to " ",
                TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe HeadlessFreeLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe emptyList<BananaContract.Token>()
        tokenStore.tokens.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts FreeLinks, while it had not been closed, if it is locatated at the End of the Message and reports a warning`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "https://example.org"

        val tokens = createTokens(
            listOf(
                TokenTypes.LITERAL to "[",
                TokenTypes.WHITESPACE to " ",
                TokenTypes.URL to word,
                TokenTypes.WHITESPACE to " ",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe HeadlessFreeLinkNode(word)
        tokenStore.capturedShiftedTokens mustBe emptyList<BananaContract.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning[0] mustBe Pair(
            Tag.PARSER,
            "Warning: FreeLink ($word) had not been closed!"
        )
    }

    // TODO Mixed on Message Level
}
