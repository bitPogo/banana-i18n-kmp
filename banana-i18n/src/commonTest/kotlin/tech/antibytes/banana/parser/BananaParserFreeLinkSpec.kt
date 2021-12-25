/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CompoundNode
import tech.antibytes.banana.ast.FreeLinkNode
import tech.antibytes.banana.ast.FunctionNode
import tech.antibytes.banana.ast.TextNode
import tech.antibytes.banana.ast.VariableNode
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.mock.parser.ParserPluginControllerStub
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.createTokens
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class BananaParserFreeLinkSpec {
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
    fun `Given parse is called it accepts FreeLinks`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(url)
        tokenStore.capturedShiftedTokens mustBe emptyList<BananaContract.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with additional spacing`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(url)
        tokenStore.capturedShiftedTokens mustBe emptyList<BananaContract.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks, while it had not been closed with its Literal and reports an error`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.ESCAPED to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(url)
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error[0] mustBe Pair(
            BananaContract.Tag.PARSER,
            "Error: Unexpected Token (${tokens[3]})!"
        )
    }

    @Test
    fun `Given parse is called it accepts FreeLinks, while it had not been closed and reports an error`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.LITERAL to "%",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(url)
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error[0] mustBe Pair(
            BananaContract.Tag.PARSER,
            "Error: Unexpected Token (${tokens[3]})!"
        )
    }

    @Test
    fun `Given parse is called it accepts FreeLinks like as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.ESCAPED to "[",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.ESCAPED to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf("[", url, "]"))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Spaces like as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "something"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to url,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf("[", " " , url, " ", "]"))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with ASCII as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to display,
                BananaContract.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            url,
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with DisplayText and extra spaces`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to display,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            url,
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Variable as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.VARIABLE to display,
                BananaContract.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            url,
            listOf(VariableNode(display))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Function as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to display,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            url,
            listOf(FunctionNode(display))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Function which contains extra spaceing as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to display,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
                BananaContract.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            url,
            listOf(FunctionNode(display))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLink with mixed values as LinkDisplay`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val displayPart1 = "efg"
        val displayPart2 = "efg"
        val displayPart3 = "hgi"
        val displayPart4 = "lmk"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.WHITESPACE to " ",
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
                BananaContract.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            url,
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
    fun `Given parse is called it accepts FreeLink with DisplayText, while it had not been closed, if it is locatated at the End of the Message and reports a warning`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to "[",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.URL to url,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to display,
                BananaContract.TokenTypes.WHITESPACE to " ",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            url,
            listOf(TextNode(listOf(display)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[4])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning[0] mustBe Pair(
            BananaContract.Tag.PARSER,
            "Warning: FreeLink had not been closed!"
        )
    }

    @Test
    fun `Given parse is called it accepts FreeLink while it encountered an unexpected token and reports an error`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        listOf("{", "[", "}").forEach { invalidLiteral ->
            val tokens = createTokens(
                listOf(
                    BananaContract.TokenTypes.LITERAL to "[",
                    BananaContract.TokenTypes.WHITESPACE to " ",
                    BananaContract.TokenTypes.URL to url,
                    BananaContract.TokenTypes.LITERAL to invalidLiteral,
                    BananaContract.TokenTypes.ASCII_STRING to "not important",
                    BananaContract.TokenTypes.LINK_END to "]",
                )
            )

            tokenStore.tokens = tokens.toMutableList()

            // When
            val message = parser.parse(tokenStore)

            // Then
            message fulfils CompoundNode::class
            message as CompoundNode
            message.children[0] mustBe FreeLinkNode(url)
            message.children[1] fulfils TextNode::class
            tokenStore.tokens.isEmpty() mustBe true
            logger.error[0] mustBe Pair(
                BananaContract.Tag.PARSER,
                "Error: Unexpected Token (${tokens[3]})!"
            )

            logger.clear()
        }
    }
}
