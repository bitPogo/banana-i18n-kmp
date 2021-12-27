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
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.mock.parser.ParserPluginControllerStub
import tech.antibytes.mock.parser.ParserPluginStub
import tech.antibytes.mock.parser.TestArgumentNode
import tech.antibytes.mock.parser.TestArgumentsNode
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.createTokens
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class BananaParserFunctionSpec {
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
    fun `Given parse is called it accepts Functions`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val functionName = "WORD"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to functionName,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(functionName)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with Identifiers`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val functionNamePart1 = "WORD1"
        val functionNamePart2 = "WORD2"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to functionNamePart1,
                BananaContract.TokenTypes.LITERAL to "_",
                BananaContract.TokenTypes.ASCII_STRING to functionNamePart2,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode("${functionNamePart1}_$functionNamePart2")
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[1], tokens[2], tokens[3])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions which contain additional spaces`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val functionName = "WORD"

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
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(functionName)
        tokenStore.capturedShiftedTokens mustBe listOf(tokens[2])
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with a Argument`() {
        // Given
        val plugin = BananaParser(logger, pluginController)
        val functionName = "name"
        val nestedArgumentId = "argument"
        val nestedArgument = TestArgumentNode()

        val nestedPlugin = ParserPluginStub { tokenizer ->
            if (tokenizer.currentToken.value != nestedArgumentId) {
                throw RuntimeException()
            }

            tokenizer.consume()
            nestedArgument
        }
        pluginController.resolvePlugin = { Pair(nestedPlugin, TestArgumentsNode) }

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to functionName,
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.ASCII_STRING to nestedArgumentId,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = plugin.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(functionName, TestArgumentsNode.lastInstance)
        TestArgumentsNode.lastChildren mustBe listOf(nestedArgument)
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with multiple Arguments`() {
        // Given
        val plugin = BananaParser(logger, pluginController)
        val functionName = "name"
        val nestedArgumentId1 = "argument1"
        val nestedArgumentId2 = "argument2"
        val nestedArguments = mutableListOf<BananaContract.Node>()

        val nestedPlugin = ParserPluginStub { tokenizer ->
            if (tokenizer.currentToken.value != nestedArgumentId1 && tokenizer.currentToken.value != nestedArgumentId2) {
                throw RuntimeException()
            }

            tokenizer.consume()
            TestArgumentNode().also {
                nestedArguments.add(it)
            }
        }
        pluginController.resolvePlugin = { Pair(nestedPlugin, TestArgumentsNode) }

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to functionName,
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.ASCII_STRING to nestedArgumentId1,
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.ASCII_STRING to nestedArgumentId2,
                BananaContract.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = plugin.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(functionName, TestArgumentsNode.lastInstance)
        TestArgumentsNode.lastChildren mustBe nestedArguments
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with multiple Arguments which contain additional spaces`() {
        // Given
        val plugin = BananaParser(logger, pluginController)
        val functionName = "name"
        val nestedArgumentId1 = "argument1"
        val nestedArgumentId2 = "argument2"
        val nestedArguments = mutableListOf<BananaContract.Node>()

        val nestedPlugin = ParserPluginStub { tokenizer ->
            if (tokenizer.currentToken.value != nestedArgumentId1 && tokenizer.currentToken.value != nestedArgumentId2) {
                throw RuntimeException()
            }

            tokenizer.consume()
            TestArgumentNode().also {
                nestedArguments.add(it)
            }
        }
        pluginController.resolvePlugin = { Pair(nestedPlugin, TestArgumentsNode) }

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.ASCII_STRING to functionName,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.LITERAL to ":",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to nestedArgumentId1,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.DELIMITER to "|",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to nestedArgumentId2,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = plugin.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(functionName, TestArgumentsNode.lastInstance)
        TestArgumentsNode.lastChildren mustBe nestedArguments
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions like syntax as Text`() {
        // Given
        val parser = BananaParser(logger, pluginController)
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
    fun `Given parse is called it accepts Functions while it had not been closed and reports a warning if it occures at the end of the message`() {
        // Given
        val parser = BananaParser(logger, pluginController)
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
            "Warning: Function had not been closed!"
        )
    }

    @Test
    fun `Given parse is called it accepts Functions if the Grammar was incorrect while reporting an Error`() {
        // Given
        val parser = BananaParser(logger, pluginController)
        val word = "abc"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.FUNCTION_START to "{{",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word,
                BananaContract.TokenTypes.ESCAPED to ":",
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.ASCII_STRING to word
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode(word)

        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error[0] mustBe Pair(
            BananaContract.Tag.PARSER,
            "Error: Unexpected Token (${tokens[3]})!"
        )
    }
}


