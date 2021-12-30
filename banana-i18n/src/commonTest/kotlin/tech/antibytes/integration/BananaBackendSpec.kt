/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.integration

import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.banana.interpreter.CompoundInterpreter
import tech.antibytes.banana.interpreter.DefaultFunctionInterpreter
import tech.antibytes.banana.interpreter.DefaultLinkFormatter
import tech.antibytes.banana.interpreter.FreeLinkInterpreter
import tech.antibytes.banana.interpreter.FunctionSelector
import tech.antibytes.banana.interpreter.InterpreterController
import tech.antibytes.banana.interpreter.LinkInterpreter
import tech.antibytes.banana.interpreter.NodeConcatenator
import tech.antibytes.banana.interpreter.TextInterpreter
import tech.antibytes.banana.interpreter.VariableInterpreter
import tech.antibytes.banana.interpreter.XMLInterceptor
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class BananaBackendSpec {
    private val logger = LoggerStub() // TODO replace with a DefaultLogger

    @Test
    fun `Given interprete is called with a parse tree it returns a the formatted text`() {
        val messages = mapOf(
            CoreNode.CompoundNode(
                listOf(
                    CoreNode.TextNode(
                        listOf("A", " ", "Client", " ", "for", " ", "the", " ", "Wikibase", " ", "extension")
                    )
                )
            ) to Pair("A Client for the Wikibase extension", emptyList()),
            CoreNode.CompoundNode(
                children = listOf(
                    CoreNode.TextNode(
                        chunks = listOf(
                            "<",
                            "p",
                            ">",
                            "<",
                            "strong",
                            ">",
                            "Warum",
                            " ",
                            "ist",
                            " ",
                            "diese",
                            " ",
                            "Seite",
                            " ",
                            "geschützt",
                            "?",
                            "<",
                            "/",
                            "strong",
                            ">",
                            "<",
                            "/",
                            "p",
                            ">",
                            "\n",
                            "<",
                            "p",
                            ">",
                            "Diese",
                            " ",
                            "Seite",
                            " ",
                            "ist",
                            " "
                        )
                    ),
                    CoreNode.LinkNode(
                        target = listOf(CoreNode.TextNode(chunks = listOf("Project", ":", "Transclusion"))),
                        display = listOf(CoreNode.TextNode(chunks = listOf("eingebunden")))
                    ),
                    CoreNode.TextNode(chunks = listOf(" ", "in", " ", "die", " ")),
                    CoreNode.FunctionNode(
                        id = "PLURAL",
                        arguments = CoreNode.CompoundNode(
                            children = listOf(
                                CoreNode.CompoundNode(children = listOf(CoreNode.VariableNode(id = "1"))),
                                CoreNode.CompoundNode(
                                    children = listOf(
                                        CoreNode.TextNode(
                                            chunks = listOf(
                                                "folgende",
                                                " ",
                                                "Seite",
                                                ",",
                                                " ",
                                                "welche",
                                                " ",
                                                "[[",
                                                "Project",
                                                ":",
                                                "Cascade"
                                            )
                                        )
                                    )
                                ),
                                CoreNode.CompoundNode(
                                    children = listOf(
                                        CoreNode.TextNode(chunks = listOf("kaskadengeschützt", "]]", " ", "ist"))
                                    )
                                ),
                                CoreNode.CompoundNode(
                                    children = listOf(
                                        CoreNode.TextNode(
                                            chunks = listOf(
                                                "folgenden",
                                                " ",
                                                "Seiten",
                                                ",",
                                                " ",
                                                "welche",
                                                " ",
                                                "[[",
                                                "Project",
                                                ":",
                                                "Cascade"
                                            )
                                        )
                                    )
                                ),
                                CoreNode.CompoundNode(
                                    children = listOf(
                                        CoreNode.TextNode(chunks = listOf("kaskadengeschützt", "]]", " ", "sind"))
                                    )
                                )
                            )
                        )
                    ),
                    CoreNode.TextNode(chunks = listOf(":", "<", "/", "p", ">", "\n")),
                    CoreNode.VariableNode(id = "2")
                )
            ) to Pair(
                "&lt;p&gt;&lt;strong&gt;Warum ist diese Seite geschützt?&lt;/strong&gt;&lt;/p&gt;\n&lt;p&gt;Diese Seite ist eingebunden in die {{PLURAL}}:&lt;/p&gt;\nanything",
                listOf(Pair(PublicApi.Tag.INTERPRETER, "Error: Unknown function PLURAL in use."))
            )

        )
        val concatenator = NodeConcatenator()
        val interpreter = InterpreterController(
            parameter = mapOf("0" to "somewhere", "1" to "someting", "2" to "anything"),
            variableInterpreter = VariableInterpreter(logger),
            textInterpreter = TextInterpreter(XMLInterceptor()),
            functionSelector = FunctionSelector(DefaultFunctionInterpreter(logger), emptyMap()),
            compoundInterpreter = CompoundInterpreter(concatenator),
            linkInterpreter = LinkInterpreter(concatenator, DefaultLinkFormatter()),
            freeLinkInterpreter = FreeLinkInterpreter(concatenator, DefaultLinkFormatter()),
        )

        for (mappedMessage in messages) {
            val message = interpreter.interpret(mappedMessage.key)

            message mustBe mappedMessage.value.first
            logger.error mustBe mappedMessage.value.second
            logger.warning.isEmpty() mustBe true

            logger.clear()
        }
    }
}
