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

class DefaultArgumentsParserSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = TokenStoreFake()
    private val logger = LoggerStub()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
        logger.clear()
    }

    @Test
    fun `It fulfils ParserPlugin`() {
        DefaultArgumentsParser(logger) fulfils BananaContract.ParserPlugin::class
    }

    @Test
    fun `Given parse is called it accepts Variables`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val variable = "1"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.VARIABLE to variable,
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe VariableNode(variable)
    }

    @Test
    fun `Given parse is called it accepts Functions`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val functionName = "name"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to functionName,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe FunctionNode(functionName)
    }

    @Test
    fun `Given parse is called it accepts spaced Functions`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val functionName = "name"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to functionName,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe FunctionNode(functionName)
    }

    @Test
    fun `Given parse is called it accepts Ascii`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "name"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.ASCII_STRING to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse is called it accepts NonAscii`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.NON_ASCII_STRING to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse is called it accepts URL`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "http://example.org"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.URL to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse is called it accepts Ingeger`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.INTEGER to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse is called it accepts Double`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.DOUBLE to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse is called it accepts Escaped`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "&"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.ESCAPED to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse is called it accepts FunctionStart`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "{{"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse is called it accepts LinkStart`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "[["

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_START to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse is called it accepts LinkEnd`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "]]"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LINK_END to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse is called it accepts Literal`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "]"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.LITERAL to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse is called it accepts Text separated by Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.ASCII_STRING to value,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value, " ", value))
    }

    @Test
    fun `Given parse is called it ignores trailing Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.ASCII_STRING to value,
                BananaContract.TokenTypes.WHITESPACE to " ",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse it called it reads until Delimiter`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.ASCII_STRING to value,
                BananaContract.TokenTypes.DELIMITER to "|",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse it called it reads until Delimiter while ignoring trailing Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.ASCII_STRING to value,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.DELIMITER to "|",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse it called it reads until FunctionEnd`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.ASCII_STRING to value,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }

    @Test
    fun `Given parse it called it reads until FunctionEnd while ignoring trailing Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser(logger)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.ASCII_STRING to value,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
    }
}
