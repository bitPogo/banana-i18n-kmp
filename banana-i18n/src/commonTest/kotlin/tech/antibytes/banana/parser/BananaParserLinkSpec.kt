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
import tech.antibytes.banana.ast.LinkNode
import tech.antibytes.banana.ast.TextNode
import tech.antibytes.banana.ast.VariableNode
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.createTokens
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class BananaParserLinkSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = TokenStoreFake()
    private val logger = LoggerStub()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
        logger.clear()
    }

    @Test
    fun `Given parse is called it accepts Links`() {
        // Given
        val parser = BananaParser(logger)
        val word = "WORD"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(word)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Links with Identifiers`() {
        // Given
        val parser = BananaParser(logger)
        val word1 = "WORD1"
        val word2 = "WORD2"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.ASCII_STRING to word1,
                BananaContract.TokenTypes.LITERAL to "_",
                BananaContract.TokenTypes.ASCII_STRING to word2,
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(word1, "_", word2)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1], tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Links which contain additional spaces`() {
        // Given
        val parser = BananaParser(logger)
        val word = "WORD"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(word)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts DOUBLE as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.DOUBLE to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(word)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts INTEGER as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.INTEGER to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(word)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts NON_ASCII as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.NON_ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(word)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts LITERAL as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = "!"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(word)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts ESCAPED as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = "{"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ESCAPED to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(word)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts VARIABLE as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = "word"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.VARIABLE to word,
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(VariableNode(word))
        )
        tokenStore.capturedShiftedTokens mustBe emptyList<BananaContract.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts VARIABLE with additional spaces as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = "word"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.VARIABLE to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(VariableNode(word))
        )
        tokenStore.capturedShiftedTokens mustBe emptyList<BananaContract.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = "word"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(FunctionNode(word))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with additional space as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = "word"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(FunctionNode(word))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions like with inner additional space as Text`() {
        // Given
        val parser = BananaParser(logger)
        val word = "word"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.NON_ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(
            listOf(
                "[[", "{{", " ", word, " ", "}}", "]]"
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions like with spacing as Text`() {
        // Given
        val parser = BananaParser(logger)
        val word = "word"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.NON_ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(
            listOf(
                "[[", " ", "{{", word, " ", "}}", " ", "]]"
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with inner additional space as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = "word"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(FunctionNode(word))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions like with all out additional space as Text`() {
        // Given
        val parser = BananaParser(logger)
        val word = "word"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to "{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.NON_ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(
            listOf(
                "[[", " ", "{", " ", word, " ", "}}", " ", "]]"
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with all out additional space as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word = "word"
        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(FunctionNode(word))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts mixed Values as Link`() {
        // Given
        val parser = BananaParser(logger)
        val word1 = "word1"
        val word2 = "word2"
        val word3 = "word3"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word1,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word2,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.VARIABLE to word3,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(word1, " ")),
                FunctionNode(word2),
                TextNode(listOf(" ")),
                VariableNode(word3)
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Link with ASCII as DisplayText`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.ASCII_STRING to display,
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(word))
            ),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Link with DisplayText with additional spaces`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to display,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(word))
            ),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Link with Illegal Link Literals as DisplayText`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"
        val display = "["

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.LITERAL to display,
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(word))
            ),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Link with Variable as LinkDisplay`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.VARIABLE to display,
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(word))
            ),
            listOf(VariableNode(display))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Link with Function as LinkDisplay`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to display,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(word))
            ),
            listOf(FunctionNode(display))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Link with spaced Function as LinkDisplay`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to display,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(word))
            ),
            listOf(FunctionNode(display))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Link with mixed values as LinkDisplay`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"
        val displayPart1 = "efg"
        val displayPart2 = "efg"
        val displayPart3 = "hgi"
        val displayPart4 = "lmk"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.ASCII_STRING to displayPart1,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to displayPart2,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.VARIABLE to displayPart3,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to displayPart4,
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(word))
            ),
            listOf(
                TextNode(listOf(displayPart1, " ")),
                FunctionNode(displayPart2),
                TextNode(listOf(" ")),
                VariableNode(displayPart3),
                TextNode(listOf(" ", displayPart4)),
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Link, while it had not been closed, if it is locatated at the End of the Message and reports a warning`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
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
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(TextNode(listOf(word)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning[0] mustBe Pair(
            BananaContract.Tag.PARSER,
            "Warning: Link had not been closed!"
        )
    }

    @Test
    fun `Given parse is called it accepts Link while it encountered an unexpected token and reports an error`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"

        listOf("{", "[", "}", "]").forEach { invalidLiteral ->
            val tokens = createTokens(
                listOf(
                    BananaContract.TokenTypes.LINK_START to "[[",
                    BananaContract.TokenTypes.WHITESPACE to " ",
                    BananaContract.TokenTypes.ASCII_STRING to word,
                    BananaContract.TokenTypes.WHITESPACE to " ",
                    BananaContract.TokenTypes.LITERAL to invalidLiteral,
                    BananaContract.TokenTypes.ASCII_STRING to "not important",
                    BananaContract.TokenTypes.LINK_END to "]]",
                )
            )

            tokenStore.tokens = tokens.toMutableList()

            // When
            val message = parser.parse(tokenStore)

            // Then
            message fulfils CompoundNode::class
            message as CompoundNode
            message.children[0] mustBe LinkNode(
                listOf(TextNode(listOf(word, " ")))
            )
            message.children[1] fulfils TextNode::class
            tokenStore.tokens.isEmpty() mustBe true
            logger.error[0] mustBe Pair(
                BananaContract.Tag.PARSER,
                "Error: Unexpected Token (${tokens[4]})!"
            )

            logger.clear()
        }
    }

    @Test
    fun `Given parse is called it accepts Link with escaped illegal Link Literals`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"

        listOf("{", "[", "}", "]").forEach { illegal ->
            val tokens = createTokens(
                listOf(
                    BananaContract.TokenTypes.LINK_START to "[[",
                    BananaContract.TokenTypes.WHITESPACE to " ",
                    BananaContract.TokenTypes.ASCII_STRING to word,
                    BananaContract.TokenTypes.WHITESPACE to " ",
                    BananaContract.TokenTypes.ESCAPED to illegal,
                    BananaContract.TokenTypes.ASCII_STRING to "not important",
                    BananaContract.TokenTypes.LINK_END to "]]",
                )
            )

            tokenStore.tokens = tokens.toMutableList()

            // When
            val message = parser.parse(tokenStore)

            // Then
            message fulfils CompoundNode::class
            message as CompoundNode
            message.children[0] mustBe LinkNode(
                listOf(TextNode(listOf(word, " ", illegal, "not important")))
            )
            tokenStore.tokens.isEmpty() mustBe true
            logger.error.isEmpty() mustBe true

            logger.clear()
        }
    }

    @Test
    fun `Given parse is called it accepts Link like with nested Function like as Text`() {
        // Given
        val parser = BananaParser(logger)
        val word = "23"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.INTEGER to word,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LINK_END to "]]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(
            listOf("[[", " ", "{{", " ", "{{", " ", word, "}}", " ", "]]")
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Link with LinkDisplay, while it had not been closed`() {
        // Given
        val parser = BananaParser(logger)
        val word = "abc"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to "[[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.ASCII_STRING to display,
                BananaContract.TokenTypes.WHITESPACE to " ",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe LinkNode(
            listOf(
                TextNode(listOf(word))
            ),
            listOf(
                TextNode(listOf(display)),
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.warning[0] mustBe Pair(
            BananaContract.Tag.PARSER,
            "Warning: Link had not been closed!"
        )
    }
}
