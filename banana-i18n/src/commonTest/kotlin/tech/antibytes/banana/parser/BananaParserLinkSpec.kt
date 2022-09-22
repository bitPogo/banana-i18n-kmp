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
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.banana.ast.CoreNode.LinkNode
import tech.antibytes.banana.ast.CoreNode.TextNode
import tech.antibytes.banana.ast.CoreNode.VariableNode
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.parser.ParserPluginControllerStub
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.createTokens
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class BananaParserLinkSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = TokenStoreFake()
    private val logger = LoggerStub()
    private val pluginController = ParserPluginControllerStub()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
        logger.clear()
    }

    @Test
    fun `Given parse is called it accepts Links`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "WORD"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(target))),
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Links with Identifiers`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target1 = "WORD1"
        val target2 = "WORD2"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.ASCII_STRING to target1,
                PublicApi.TokenTypes.LITERAL to "_",
                PublicApi.TokenTypes.ASCII_STRING to target2,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(target1, "_", target2))),
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1], tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Links which contain additional spaces`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "WORD"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(target))),
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts DOUBLE as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = fixture.fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.DOUBLE to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(target))),
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts INTEGER as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = fixture.fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.INTEGER to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(target))),
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts NON_ASCII as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.NON_ASCII_STRING to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(target))),
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts LITERAL as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "!"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LITERAL to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(target))),
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts ESCAPED as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "{"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ESCAPED to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(target))),
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts VARIABLE as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "word"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.VARIABLE to target,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(VariableNode(target)),
        )
        tokenStore.capturedShiftedTokens mustBe emptyList<PublicApi.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts VARIABLE with additional spaces as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "word"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.VARIABLE to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(VariableNode(target)),
        )
        tokenStore.capturedShiftedTokens mustBe emptyList<PublicApi.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Functions as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "word"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(FunctionNode(target.uppercase())),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Functions with additional space as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "word"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(FunctionNode(target.uppercase())),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Functions like with inner additional space as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "word"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.NON_ASCII_STRING to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(
            listOf(
                "[[",
                "{{",
                " ",
                target,
                " ",
                "}}",
                "]]",
            ),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Functions like with spacing as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "word"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.NON_ASCII_STRING to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(
            listOf(
                "[[",
                " ",
                "{{",
                target,
                " ",
                "}}",
                " ",
                "]]",
            ),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Functions with inner additional space as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "word"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(FunctionNode(target.uppercase())),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Functions like with all out additional space as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "word"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LITERAL to "{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.NON_ASCII_STRING to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(
            listOf(
                "[[", " ", "{", " ", target, " ", "}}", " ", "]]",
            ),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Functions with all out additional space as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "word"
        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(FunctionNode(target.uppercase())),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts mixed Values as Link`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target1 = "word1"
        val target2 = "word2"
        val target3 = "word3"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target1,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target2,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.VARIABLE to target3,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target1, " ")),
                FunctionNode(target2.uppercase()),
                TextNode(listOf(" ")),
                VariableNode(target3),
            ),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with ASCII as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.ASCII_STRING to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with NonAscii as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.NON_ASCII_STRING to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with Double as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = fixture.fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.DOUBLE to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with Integer as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = fixture.fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.INTEGER to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with Escaped as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "}"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.ESCAPED to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with Literal as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "&"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.LITERAL to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with Delimiter as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "|"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.DELIMITER to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with FunctionStart as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "{{"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.FUNCTION_START to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with FunctionEnd as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "}}"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.FUNCTION_END to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with LinkStart as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "[["

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.LINK_START to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with DisplayText with additional spaces`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to display,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with Space separated DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val displayPart1 = "a"
        val displayPart2 = "b"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.ASCII_STRING to displayPart1,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to displayPart2,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(displayPart1, " ", displayPart2))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with Illegal Link Literals as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "["

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.LITERAL to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(TextNode(listOf(display))),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with Variable as LinkDisplay`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.VARIABLE to display,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(VariableNode(display)),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with Function as LinkDisplay`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.ASCII_STRING to display,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(FunctionNode(display.uppercase())),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with spaced Function as LinkDisplay`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to display,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(FunctionNode(display.uppercase())),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with mixed values as LinkDisplay`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val displayPart1 = "efg"
        val displayPart2 = "efg"
        val displayPart3 = "hgi"
        val displayPart4 = "lmk"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.ASCII_STRING to displayPart1,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to displayPart2,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.VARIABLE to displayPart3,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to displayPart4,
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(
                TextNode(listOf(displayPart1, " ")),
                FunctionNode(displayPart2.uppercase()),
                TextNode(listOf(" ")),
                VariableNode(displayPart3),
                TextNode(listOf(" ", displayPart4)),
            ),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link, while it had not been closed, if it is locatated at the End of the Message and reports a warning`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.WHITESPACE to " ",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(target))),
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning[0] mustBe Pair(
            PublicApi.Tag.PARSER,
            "Warning: Link had not been closed!",
        )
    }

    @Test
    fun `Given parse is called it accepts Link while it encountered an unexpected token and reports an error`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"

        listOf("{", "[", "}", "]").forEach { invalidLiteral ->
            val tokens = createTokens(
                listOf(
                    PublicApi.TokenTypes.LINK_START to "[[",
                    PublicApi.TokenTypes.WHITESPACE to " ",
                    PublicApi.TokenTypes.ASCII_STRING to target,
                    PublicApi.TokenTypes.WHITESPACE to " ",
                    PublicApi.TokenTypes.LITERAL to invalidLiteral,
                    PublicApi.TokenTypes.ASCII_STRING to "not important",
                    PublicApi.TokenTypes.LINK_END to "]]",
                ),
            )

            tokenStore.tokens = tokens.toMutableList()

            // When
            val message = parser.parse(tokenStore)

            // Then
            message fulfils CompoundNode::class
            message as CompoundNode
            message.children[0] mustBe LinkNode(
                listOf(TextNode(listOf(target, " "))),
            )
            message.children[1] fulfils TextNode::class
            tokenStore.tokens.isEmpty() mustBe true
            logger.error[0] mustBe Pair(
                PublicApi.Tag.PARSER,
                "Error: Unexpected Token (${tokens[4]})!",
            )

            logger.clear()
        }
    }

    @Test
    fun `Given parse is called it accepts Link with escaped illegal Link Literals`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"

        listOf("{", "[", "}", "]").forEach { illegal ->
            val tokens = createTokens(
                listOf(
                    PublicApi.TokenTypes.LINK_START to "[[",
                    PublicApi.TokenTypes.WHITESPACE to " ",
                    PublicApi.TokenTypes.ASCII_STRING to target,
                    PublicApi.TokenTypes.WHITESPACE to " ",
                    PublicApi.TokenTypes.ESCAPED to illegal,
                    PublicApi.TokenTypes.ASCII_STRING to "not important",
                    PublicApi.TokenTypes.LINK_END to "]]",
                ),
            )

            tokenStore.tokens = tokens.toMutableList()

            // When
            val message = parser.parse(tokenStore)

            // Then
            message fulfils CompoundNode::class
            message as CompoundNode
            message.children[0] mustBe LinkNode(
                listOf(TextNode(listOf(target, " ", illegal, "not important"))),
            )
            tokenStore.tokens.isEmpty() mustBe true
            logger.error.isEmpty() mustBe true

            logger.clear()
        }
    }

    @Test
    fun `Given parse is called it accepts Link like with nested Function like as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "23"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.INTEGER to target,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to "]]",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(
            listOf("[[", " ", "{{", " ", "{{", " ", target, "}}", " ", "]]"),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Link with LinkDisplay, while it had not been closed`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val target = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to target,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.ASCII_STRING to display,
                PublicApi.TokenTypes.WHITESPACE to " ",
            ),
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(target)),
            ),
            listOf(
                TextNode(listOf(display)),
            ),
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.error mustBe emptyList()
        logger.warning[0] mustBe Pair(
            PublicApi.Tag.PARSER,
            "Warning: Link had not been closed!",
        )
    }
}
