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
    fun `It fulfils ParserPlugin`() {
        DefaultArgumentsParser(logger, pluginController) fulfils BananaContract.ParserPlugin::class
    }

    @Test
    fun `Given parse is called it accepts Variables`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts spaced Functions`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with a Argument`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe FunctionNode(functionName, TestArgumentsNode.lastInstance)
        TestArgumentsNode.lastChildren mustBe listOf(nestedArgument)
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with multiple Arguments`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe FunctionNode(functionName, TestArgumentsNode.lastInstance)
        TestArgumentsNode.lastChildren mustBe nestedArguments
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Functions with multiple Arguments which contain additional spaces`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        val argument = plugin.parse(tokenStore)

        // Then
        argument fulfils CompoundNode::class
        argument as CompoundNode
        argument.children[0] mustBe FunctionNode(functionName, TestArgumentsNode.lastInstance)
        TestArgumentsNode.lastChildren mustBe nestedArguments
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Ascii`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts NonAscii`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts URL`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Ingeger`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Double`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Escaped`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts FunctionStart`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts LinkStart`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts LinkEnd`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Literal`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it accepts Text separated by Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called it ignores trailing Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse it called it reads until Delimiter`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse it called it reads until Delimiter while ignoring trailing Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse it called it reads until FunctionEnd`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse it called it reads until FunctionEnd while ignoring trailing Whitespaces`() {
        // Given
        val plugin = DefaultArgumentsParser(logger, pluginController)
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
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }

    @Test
    fun `Given parse is called, it accepts mixed values`() {
        val plugin = DefaultArgumentsParser(logger, pluginController)
        val variable = "1"
        val string = "something"
        val functionName = "name"

        val tokens = createTokens(
            listOf(
                BananaContract.TokenTypes.ASCII_STRING to string,
                BananaContract.TokenTypes.WHITESPACE to " ",
                BananaContract.TokenTypes.VARIABLE to variable,
                BananaContract.TokenTypes.WHITESPACE to " ",
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
        argument.children[0] mustBe TextNode(listOf(string, " "))
        argument.children[1] mustBe VariableNode(variable)
        argument.children[2] mustBe TextNode(listOf(" "))
        argument.children[3] mustBe FunctionNode(functionName)
        tokenStore.tokens.isEmpty() mustBe true
        logger.warning mustBe emptyList<Pair<BananaContract.Tag, String>>()
        logger.error mustBe emptyList<Pair<BananaContract.Tag, String>>()
    }
}
