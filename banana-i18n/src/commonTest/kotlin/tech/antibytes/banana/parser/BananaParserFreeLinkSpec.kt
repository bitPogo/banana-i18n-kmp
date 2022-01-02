/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode.CompoundNode
import tech.antibytes.banana.ast.CoreNode.FreeLinkNode
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.banana.ast.CoreNode.TextNode
import tech.antibytes.banana.ast.CoreNode.VariableNode
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.mock.parser.ParserPluginControllerStub
import tech.antibytes.mock.parser.ParserEngineFake
import tech.antibytes.util.createTokens
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class BananaParserFreeLinkSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = ParserEngineFake()
    private val logger = LoggerStub()
    private val pluginController = ParserPluginControllerStub()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
        logger.clear()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with URL`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(TextNode(listOf(url)))
        tokenStore.capturedShiftedTokens mustBe emptyList<PublicApi.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Variable`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val variable = "url"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.VARIABLE to variable,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(VariableNode(variable))
        tokenStore.capturedShiftedTokens mustBe emptyList<PublicApi.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks by Url with additional spacing`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(TextNode(listOf(url)))
        tokenStore.capturedShiftedTokens mustBe emptyList<PublicApi.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks by Variable with additional spacing`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val variable = "name"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.VARIABLE to variable,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(VariableNode(variable))
        tokenStore.capturedShiftedTokens mustBe emptyList<PublicApi.Token>()
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks, while it had not been closed with its Literal and reports an error`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.ESCAPED to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(TextNode(listOf(url)))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error[0] mustBe Pair(
            PublicApi.Tag.PARSER,
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
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.LITERAL to "%",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(TextNode(listOf(url)))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error[0] mustBe Pair(
            PublicApi.Tag.PARSER,
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
                PublicApi.TokenTypes.ESCAPED to "[",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.ESCAPED to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf("[", url, "]"))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Spaces like as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val string = "something"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to string,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe TextNode(listOf("[", " ", string, " ", "]"))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with ASCII as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with NonAscii as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.NON_ASCII_STRING to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Double as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.DOUBLE to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Integer as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.INTEGER to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Escaped as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "]"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ESCAPED to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Literal as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = ":"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LITERAL to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with URL as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LITERAL to url,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(url)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with FunctionStart as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "{{"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with FunctionEnd as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "}}"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with LinkStart as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "[["

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_START to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with LinkEnd as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "]]"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LINK_END to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Delimiter as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "|"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.DELIMITER to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Space separated as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val displayPart1 = "Hello"
        val displayPart2 = "World"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to displayPart1,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to displayPart2,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(displayPart1, " ", displayPart2)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with DisplayText and extra spaces`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to display,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Variable as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.VARIABLE to display,
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(VariableNode(display))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Function as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.ASCII_STRING to display,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(FunctionNode(display))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLinks with Function which contains extra spaceing as DisplayText`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to display,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(FunctionNode(display))
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
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
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
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
                PublicApi.TokenTypes.LITERAL to "]",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(
                TextNode(listOf(displayPart1, " ")),
                FunctionNode(displayPart2),
                TextNode(listOf(" ")),
                VariableNode(displayPart3),
                TextNode(listOf(" ", displayPart4)),
            )
        )
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<PublicApi.Tag, String>>()
        logger.error mustBe emptyList<Pair<PublicApi.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FreeLink with DisplayText, while it had not been closed, if it is locatated at the End of the Message and reports a warning`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val url = "https://example.org"
        val display = "efg"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.URL to url,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to display,
                PublicApi.TokenTypes.WHITESPACE to " ",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FreeLinkNode(
            TextNode(listOf(url)),
            listOf(TextNode(listOf(display)))
        )
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[4])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning[0] mustBe Pair(
            PublicApi.Tag.PARSER,
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
                    PublicApi.TokenTypes.LITERAL to "[",
                    PublicApi.TokenTypes.WHITESPACE to " ",
                    PublicApi.TokenTypes.URL to url,
                    PublicApi.TokenTypes.LITERAL to invalidLiteral,
                    PublicApi.TokenTypes.ASCII_STRING to "not important",
                    PublicApi.TokenTypes.LINK_END to "]",
                )
            )

            tokenStore.tokens = tokens.toMutableList()

            // When
            val message = parser.parse(tokenStore)

            // Then
            message fulfils CompoundNode::class
            message as CompoundNode
            message.children[0] mustBe FreeLinkNode(TextNode(listOf(url)))
            message.children[1] fulfils TextNode::class
            tokenStore.tokens.isEmpty() mustBe true
            logger.error[0] mustBe Pair(
                PublicApi.Tag.PARSER,
                "Error: Unexpected Token (${tokens[3]})!"
            )

            logger.clear()
        }
    }
}
