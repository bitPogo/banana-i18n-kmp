/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode.CompoundNode
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.banana.ast.CoreNode.TextNode
import tech.antibytes.banana.ast.CoreNode.VariableNode
import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.parser.ParserPluginControllerStub
import tech.antibytes.mock.parser.ParserPluginStub
import tech.antibytes.mock.parser.TestArgumentNode
import tech.antibytes.mock.parser.TestArgumentsNode
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.createTokens
import tech.antibytes.util.test.fixture.fixture
import tech.antibytes.util.test.fixture.kotlinFixture
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class DefaultArgumentsParserSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = TokenStoreFake()
    private val logger = LoggerStub()
    private val pluginController = ParserPluginControllerStub()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
        logger.clear()
        pluginController.clear()
    }

    @Test
    fun `It fulfils ParserPluginFactory`() {
        DefaultArgumentsParser fulfils PublicApi.ParserPluginFactory::class
    }

    @Test
    fun `Given createPlugin is called with a Logger and PluginController it creates a ParserPlugin`() {
        DefaultArgumentsParser.createPlugin(logger, pluginController) fulfils PublicApi.ParserPlugin::class
    }

    @Test
    fun `Given parse is called it accepts Variables`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val variable = "1"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.VARIABLE to variable,
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
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val functionName = "name"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.ASCII_STRING to functionName,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe FunctionNode(functionName.uppercase())
    }

    @Test
    fun `Given parse is called it accepts Functions with Identifiers`() {
        // Given
        val parser = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val functionNamePart1 = "WORD1"
        val functionNamePart2 = "WORD2"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.ASCII_STRING to functionNamePart1,
                PublicApi.TokenTypes.LITERAL to "_",
                PublicApi.TokenTypes.ASCII_STRING to functionNamePart2,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children[0] mustBe FunctionNode("${functionNamePart1}_$functionNamePart2")
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts spaced Functions`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val functionName = "name"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to functionName,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe FunctionNode(functionName.uppercase())
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Functions with a Argument`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
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
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.ASCII_STRING to functionName,
                PublicApi.TokenTypes.LITERAL to ":",
                PublicApi.TokenTypes.ASCII_STRING to nestedArgumentId,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe FunctionNode(functionName.uppercase(), TestArgumentsNode.lastInstance)
        TestArgumentsNode.lastChildren mustBe listOf(nestedArgument)
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Functions with multiple Arguments`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val functionName = "name"
        val nestedArgumentId1 = "argument1"
        val nestedArgumentId2 = "argument2"
        val nestedArguments = mutableListOf<PublicApi.Node>()

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
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.ASCII_STRING to functionName,
                PublicApi.TokenTypes.LITERAL to ":",
                PublicApi.TokenTypes.ASCII_STRING to nestedArgumentId1,
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.ASCII_STRING to nestedArgumentId2,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe FunctionNode(functionName.uppercase(), TestArgumentsNode.lastInstance)
        TestArgumentsNode.lastChildren mustBe nestedArguments
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Functions with multiple Arguments which contain additional spaces`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val functionName = "name"
        val nestedArgumentId1 = "argument1"
        val nestedArgumentId2 = "argument2"
        val nestedArguments = mutableListOf<PublicApi.Node>()

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
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.ASCII_STRING to functionName,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.LITERAL to ":",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to nestedArgumentId1,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.DELIMITER to "|",
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to nestedArgumentId2,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe FunctionNode(functionName.uppercase(), TestArgumentsNode.lastInstance)
        TestArgumentsNode.lastChildren mustBe nestedArguments
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Ascii`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "name"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts NonAscii`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "ηὕρηκα"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.NON_ASCII_STRING to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts URL`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "http://example.org"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.URL to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Ingeger`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = fixture.fixture<Int>().toString()

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.INTEGER to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Double`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = fixture.fixture<Double>().toString()

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.DOUBLE to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Escaped`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "&"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ESCAPED to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts FunctionStart`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "{{"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.FUNCTION_START to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts LinkStart`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "[["

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_START to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts LinkEnd`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "]]"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LINK_END to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Literal`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "]"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.LITERAL to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it accepts Text separated by Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to value,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.ASCII_STRING to value
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value, " ", value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called it ignores trailing Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to value,
                PublicApi.TokenTypes.WHITESPACE to " ",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse it called it reads until Delimiter`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to value,
                PublicApi.TokenTypes.DELIMITER to "|",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse it called it reads until Delimiter while ignoring trailing Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to value,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.DELIMITER to "|",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse it called it reads until FunctionEnd`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to value,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse it called it reads until FunctionEnd while ignoring trailing Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val value = "abc"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to value,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(value))
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }

    @Test
    fun `Given parse is called, it accepts mixed values`() {
        val plugin = DefaultArgumentsParser.createPlugin(logger, pluginController)
        val variable = "1"
        val string = "something"
        val functionName = "name"

        val tokens = createTokens(
            listOf(
                PublicApi.TokenTypes.ASCII_STRING to string,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.VARIABLE to variable,
                PublicApi.TokenTypes.WHITESPACE to " ",
                PublicApi.TokenTypes.FUNCTION_START to "{{",
                PublicApi.TokenTypes.ASCII_STRING to functionName,
                PublicApi.TokenTypes.FUNCTION_END to "}}",
            )
        )

        tokenStore.tokens = tokens.toMutableList()

        // When
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe TextNode(listOf(string, " "))
        argument.children[1] mustBe VariableNode(variable)
        argument.children[2] mustBe TextNode(listOf(" "))
        argument.children[3] mustBe FunctionNode(functionName.uppercase())
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList()
        logger.error mustBe emptyList()
    }
}
