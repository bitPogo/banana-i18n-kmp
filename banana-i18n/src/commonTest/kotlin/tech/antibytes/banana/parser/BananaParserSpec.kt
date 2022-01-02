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
import tech.antibytes.banana.ast.CoreNode.CompoundNode
import tech.antibytes.banana.ast.CoreNode.FreeLinkNode
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.banana.ast.CoreNode.LinkNode
import tech.antibytes.banana.ast.CoreNode.TextNode
import tech.antibytes.banana.ast.CoreNode.VariableNode
import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.parser.ParserPluginControllerStub
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.createTokens
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class BananaParserSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = TokenStoreFake()
    private val logger = LoggerStub()
    private val pluginController = ParserPluginControllerStub()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
    }

    @Test
    fun `It fulfils TopLevelParser`() {
        val parser: Any = BananaParser(logger, pluginController)

        parser fulfils BananaContract.TopLevelParser::class
    }

    @Test
    fun `Given parse is called it accepts Empty Messages`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        tokenStore.tokens.addAll(listOf(EOF, EOF))

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children.isEmpty() mustBe true
    }

    @Test
    fun `Given parse is called it accepts VARIABLE as Variable`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val variable = "1"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.VARIABLE to variable,
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe VariableNode(variable)
        tokenStore.capturedShiftedTokens mustBe emptyList<PublicApi.Token>()
    }

    @Test
    fun `Given parse is called it accepts mixed values`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val word1 = "abc"
        val word2 = "def"
        val word3 = "ghi"
        val word4 = "lop"
        val word5 = "lop"
        val word6 = "qrs"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to word1,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to word2,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LITERAL to "[",
                PublicApi.TokenTypes.URL to word3,
                PublicApi.TokenTypes.LITERAL to "]",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
                PublicApi.TokenTypes.LINK_START to "[[",
                PublicApi.TokenTypes.VARIABLE to word4,
                PublicApi.TokenTypes.LINK_END to "]]",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to word5,
                PublicApi.TokenTypes.VARIABLE to word6,
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children.isEmpty() mustBe false
        message.children[0] mustBe TextNode(listOf(word1, " "))
        message.children[1] mustBe FunctionNode(word2.uppercase())
        message.children[2] mustBe TextNode(listOf(" "))
        message.children[3] mustBe FreeLinkNode(TextNode(listOf(word3)))
        message.children[4] mustBe TextNode(
            listOf(" ", "{{", " ", "{{", "}}",)
        )
        message.children[5] mustBe LinkNode(
            listOf(VariableNode(word4))
        )
        message.children[6] mustBe TextNode(listOf(" ", word5))
        message.children[7] mustBe VariableNode(word6)
    }
}
