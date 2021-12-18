/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CompoundNode
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

class TopLevelParserFunctionSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = TokenStoreFake()
    private val logger = LoggerStub()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
    }

    @Test
    fun `Given parse is called it accepts VARIABLE as Variable`() {
        // Given
        val parser = TopLevelParser(logger)
        val variable = "1"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.VARIABLE to variable,
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
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with Identifiers`() {
        // Given
        val parser = TopLevelParser(logger)
        val word1 = "WORD1"
        val word2 = "WORD2"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to word1,
                BananaContract.TokenTypes.LITERAL to "_",
                BananaContract.TokenTypes.ASCII_STRING to word2,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions which contain additional spaces`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "WORD"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with ASCII as single Argument as Text which contain additional spacing`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "WORD2"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to argument,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(
                        TextNode(listOf(argument))
                    )
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with NON_ASCII as single Argument as Text`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.NON_ASCII_STRING to argument,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(
                        TextNode(listOf(argument))
                    )
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with DOUBLE as single Argument as Text`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.DOUBLE to argument,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(
                        TextNode(listOf(argument))
                    )
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with INTEGER as single Argument as Text`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.INTEGER to argument,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(
                        TextNode(listOf(argument))
                    )
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with ESCAPED as single Argument as Text`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "\\{"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.ESCAPED to argument,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(
                        TextNode(listOf(argument))
                    )
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with LITERAL as single Argument as Text`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "{"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.LITERAL to argument,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(
                        TextNode(listOf(argument))
                    )
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with WHITESPACES as single Argument as Text`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "{"
        val space = " "

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.LITERAL to argument,
                BananaContract.TokenTypes.WHITESPACE to space,
                BananaContract.TokenTypes.LITERAL to argument,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(
                        TextNode(listOf(argument, space, argument))
                    )
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with VARIABLE as single Argument as Variable`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "var"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.VARIABLE to argument,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(
                        VariableNode(argument)
                    )
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with a nested Function as single Argument as Variable`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument = "name"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to argument,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(
                        FunctionNode(argument)
                    )
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with mixed values as single Argument as Variable`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argumentPart1 = "name"
        val argumentPart2 = " "
        val argumentPart3 = "something"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.VARIABLE to argumentPart1,
                BananaContract.TokenTypes.WHITESPACE to argumentPart2,
                BananaContract.TokenTypes.ASCII_STRING to argumentPart3,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(VariableNode(argumentPart1), TextNode(listOf(argumentPart2, argumentPart3)))
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with multible Arguments`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument1 = "name"
        val argument2 = "something"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.VARIABLE to argument1,
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.ASCII_STRING to argument2,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(VariableNode(argument1))
                ),
                CompoundNode(
                    listOf(TextNode(listOf(argument2)))
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with multible spaced Arguments`() {
        // Given
        val parser = TopLevelParser(logger)
        val name = "WORD1"
        val argument1 = "name"
        val argument2 = "something"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to name,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.VARIABLE to argument1,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to argument2,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
                CompoundNode(
                    listOf(VariableNode(argument1))
                ),
                CompoundNode(
                    listOf(TextNode(listOf(argument2)))
                )
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions like syntax as Text`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.NON_ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
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
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions while it had not been closed and reports a warning`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "abc"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
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
            BananaContract.Tag.PARSER,
            "Warning: Function ($word) had not been closed!"
        )
    }

    @Test
    fun `Given parse is called it accepts Error if the Grammar was incorrect`() {
        // Given
        val parser = TopLevelParser(logger)
        val word = "abc"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ESCAPED to ":",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(word)

        tokenStore.tokens.isEmpty() mustBe true
        logger.warning[0] mustBe Pair(
            BananaContract.Tag.PARSER,
            "Warning: Function ($word) had not been closed!"
        )
    }
}
