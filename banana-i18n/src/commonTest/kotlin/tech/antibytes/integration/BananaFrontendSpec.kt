/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.integration

import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.banana.ast.CoreNode.CompoundNode
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.banana.ast.CoreNode.LinkNode
import tech.antibytes.banana.ast.CoreNode.TextNode
import tech.antibytes.banana.ast.CoreNode.VariableNode
import tech.antibytes.banana.parser.BananaParser
import tech.antibytes.banana.parser.DefaultArgumentsParser
import tech.antibytes.banana.parser.ParserEngine
import tech.antibytes.banana.parser.ParserPluginController
import tech.antibytes.banana.tokenizer.BananaTokenizer
import tech.antibytes.banana.tokenizer.StringReader
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class BananaFrontendSpec {
    private val logger = LoggerStub()

    @Test
    fun `Given parse is called with a Tokenizer, it accepts`() {
        val messages = mapOf(
            "A Client for the Wikibase extension" to
                CompoundNode(
                    listOf(
                        TextNode(
                            listOf("A", " ", "Client", " ", "for", " ", "the", " ", "Wikibase", " ", "extension")
                        )
                    )
                ),
            "<p><strong>Warum ist diese Seite geschützt?</strong></p>\n<p>Diese Seite ist [[Project:Transclusion|eingebunden]] in die {{PLURAL:$1|folgende Seite, welche [[Project:Cascade|kaskadengeschützt]] ist|folgenden Seiten, welche [[Project:Cascade|kaskadengeschützt]] sind}}:</p>\n$2" to
                CompoundNode(
                    children = listOf(
                        TextNode(
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
                        LinkNode(
                            target = listOf(TextNode(chunks = listOf("Project", ":", "Transclusion"))),
                            display = listOf(TextNode(chunks = listOf("eingebunden")))
                        ),
                        TextNode(chunks = listOf(" ", "in", " ", "die", " ")),
                        FunctionNode(
                            id = "PLURAL",
                            arguments = CompoundNode(
                                children = listOf(
                                    CompoundNode(children = listOf(VariableNode(id = "1"))),
                                    CompoundNode(
                                        children = listOf(
                                            TextNode(
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
                                    CompoundNode(
                                        children = listOf(
                                            TextNode(chunks = listOf("kaskadengeschützt", "]]", " ", "ist"))
                                        )
                                    ),
                                    CompoundNode(
                                        children = listOf(
                                            TextNode(
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
                                    CompoundNode(
                                        children = listOf(
                                            TextNode(chunks = listOf("kaskadengeschützt", "]]", " ", "sind"))
                                        )
                                    )
                                )
                            )
                        ),
                        TextNode(chunks = listOf(":", "<", "/", "p", ">", "\n")),
                        VariableNode(id = "2")
                    )
                ),
            "Der Link auf diese Seite sollte von [$1 dem dazugehörigen {{WBREPONAME}}-Objekt] entfernt worden sein. Wir bitten dich zu prüfen, ob dies geschehen ist." to
                CompoundNode(
                    children = listOf(
                        TextNode(
                            chunks = listOf(
                                "Der",
                                " ",
                                "Link",
                                " ",
                                "auf",
                                " ",
                                "diese",
                                " ",
                                "Seite",
                                " ",
                                "sollte",
                                " ",
                                "von",
                                " "
                            )
                        ),
                        CoreNode.FreeLinkNode(
                            url = VariableNode(id = "1"),
                            display = listOf(
                                TextNode(chunks = listOf("dem", " ", "dazugehörigen", " ")),
                                FunctionNode(id = "WBREPONAME"),
                                TextNode(chunks = listOf("-", "Objekt"))
                            )
                        ),
                        TextNode(
                            chunks = listOf(
                                " ",
                                "entfernt",
                                " ",
                                "worden",
                                " ",
                                "sein", ".",
                                " ",
                                "Wir",
                                " ",
                                "bitten",
                                " ",
                                "dich",
                                " ",
                                "zu",
                                " ",
                                "prüfen",
                                ",",
                                " ",
                                "ob",
                                " ",
                                "dies",
                                " ",
                                "geschehen",
                                " ",
                                "ist", "."
                            )
                        )
                    )
                )
        )

        val parser = BananaParser(
            logger,
            ParserPluginController(
                logger,
                defaultPlugin = Pair(
                    DefaultArgumentsParser,
                    CompoundNode
                )
            )
        )

        for (mappedMessage in messages) {
            val tokens = ParserEngine(
                BananaTokenizer(
                    StringReader(mappedMessage.key)
                )
            )

            val message = parser.parse(tokens)

            message mustBe mappedMessage.value
            logger.error.isEmpty() mustBe true
            logger.warning.isEmpty() mustBe true
        }
    }
}
