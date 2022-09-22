/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.integration

import kotlin.test.Test
import tech.antibytes.banana.BananaBuilder
import tech.antibytes.banana.Locale
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.banana.interpreter.XMLInterceptor
import tech.antibytes.banana.parser.SharedParserRules
import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.createLocale
import tech.antibytes.util.test.annotations.RobolectricTestRunner
import tech.antibytes.util.test.annotations.RunWithRobolectricTestRunner
import tech.antibytes.util.test.mustBe

@RunWithRobolectricTestRunner(RobolectricTestRunner::class)
class FullIntegration {
    val messages = listOf(
        "Sprachlink hinzugefügt: $1",
        "Sprachlink hinzugefügt: \$lang",
        "<p><strong>Warum ist diese Seite geschützt?</strong></p>\n<p>Diese Seite ist [[Project:Transclusion|eingebunden]] in die {{PLURAL:$1|folgende Seite, welche [[Project:Cascade|kaskadengeschützt]] ist|folgenden Seiten, welche [[Project:Cascade|kaskadengeschützt]] sind}}:</p>\n$2",
        "Die Seite, die du verlinken willst, ist bereits einem [https://en.wikipedia.org/wiki/GLR_parser Objekt] in unserem zentralen Datenrepositorium zugeordnet, das auf [[GLR parser]] auf dieser Website verlinkt. Es kann nur eine Seite pro Website einem Objekt zugeordnet werden. Bitte wähle eine andere Seite, die verlinkt werden soll.",
        "Das {{WBREPONAME}}-Objekt wurde geändert",
        "{{CAP: links}} bearbeiten",
    )

    val i18nBuilder = BananaBuilder(createLocale("de-DE"))

    @Test
    fun `It uses provided variabales`() {
        // Given
        val variable = "Schau hier"
        val i18n = i18nBuilder.build()

        // When
        val result = i18n.i18n(messages[0], variable)

        // Then
        result mustBe "Sprachlink hinzugefügt: $variable"
    }

    @Test
    fun `It uses provided named variabales`() {
        // Given
        val variable = "Schau hier"
        val i18n = i18nBuilder.build()

        // When
        val result = i18n.i18n(messages[1], mapOf("lang" to "Schau hier"))

        // Then
        result mustBe "Sprachlink hinzugefügt: $variable"
    }

    @Test
    fun `It uses the DefaultLogger`() {
        // Given
        val i18n = i18nBuilder.build()

        // When
        val result = i18n.i18n(messages[2])

        // Then
        result mustBe "<p><strong>Warum ist diese Seite geschützt?</strong></p>\n<p>Diese Seite ist eingebunden in die {{PLURAL}}:</p>\n$2"
    }

    @Test
    fun `It uses a given Logger`() {
        // Given
        val logger = LoggerStub()
        val i18n = i18nBuilder
            .setLogger(logger)
            .build()

        // When
        val result = i18n.i18n(messages[2])

        // Then
        result mustBe "<p><strong>Warum ist diese Seite geschützt?</strong></p>\n<p>Diese Seite ist eingebunden in die {{PLURAL}}:</p>\n\$2"
        logger.error[0] mustBe Pair(PublicApi.Tag.INTERPRETER, "Error: Unknown function PLURAL in use.")
    }

    @Test
    fun `It uses a given TextInterceptor`() {
        // Given
        val i18n = i18nBuilder
            .setTextInterceptor(XMLInterceptor())
            .build()

        // When
        val result = i18n.i18n(messages[2])

        // Then
        result mustBe "&lt;p&gt;&lt;strong&gt;Warum ist diese Seite geschützt?&lt;/strong&gt;&lt;/p&gt;\n&lt;p&gt;Diese Seite ist eingebunden in die {{PLURAL}}:&lt;/p&gt;\n\$2"
    }

    @Test
    fun `It uses the DefaultLinkFormatter`() {
        // Given
        val i18n = i18nBuilder
            .build()

        // When
        val result = i18n.i18n(messages[3])

        // Then
        result mustBe "Die Seite, die du verlinken willst, ist bereits einem Objekt in unserem zentralen Datenrepositorium zugeordnet, das auf GLR parser auf dieser Website verlinkt. Es kann nur eine Seite pro Website einem Objekt zugeordnet werden. Bitte wähle eine andere Seite, die verlinkt werden soll."
    }

    @Test
    fun `It uses a given LinkFormatter`() {
        // Given
        val i18n = i18nBuilder
            .setLinkFormatter(TestLinkFormatter)
            .build()

        // When
        val result = i18n.i18n(messages[3])

        // Then
        result mustBe "Die Seite, die du verlinken willst, ist bereits einem <a href=\"https://en.wikipedia.org/wiki/GLR_parser\">Objekt</a> in unserem zentralen Datenrepositorium zugeordnet, das auf <a href=\"https://en.wikipedia.org/wiki/GLR_parser\">hier</a> auf dieser Website verlinkt. Es kann nur eine Seite pro Website einem Objekt zugeordnet werden. Bitte wähle eine andere Seite, die verlinkt werden soll."
    }

    @Test
    fun `It uses given InterpreterPlugins`() {
        // Given
        val i18n = i18nBuilder
            .registerPlugin(
                PublicApi.Plugin(
                    "wbreponame",
                    RepoNameInterpreter,
                ),
            ).build()

        // When
        val result = i18n.i18n(messages[4])

        // Then
        result mustBe "Das BananaI18n-Objekt wurde geändert"
    }

    @Test
    fun `It uses given FullPlugins`() {
        // Given
        val i18n = i18nBuilder
            .registerPlugin(
                PublicApi.Plugin(
                    "cap",
                    CapitalizeInterpreter,
                    Pair(CapitalizeParser, CoreNode.CompoundNode),
                ),
            ).build()

        // When
        val result = i18n.i18n(messages[5])

        // Then
        result mustBe "Links bearbeiten"
    }
}

private object TestLinkFormatter : PublicApi.LinkFormatter {
    override fun formatLink(target: String, displayText: String): String {
        val linkTarget = target.replace(" ", "_")

        return "<a href=\"https://en.wikipedia.org/wiki/$linkTarget\">hier</a>"
    }

    override fun formatFreeLink(url: String, displayText: String): String {
        return "<a href=\"$url\">$displayText</a>"
    }
}

private class RepoNameInterpreter : PublicApi.CustomInterpreter {
    override fun interpret(
        node: CoreNode.FunctionNode,
        controller: PublicApi.InterpreterController,
    ): String = "BananaI18n"

    companion object : PublicApi.InterpreterFactory {
        override fun getInstance(
            logger: PublicApi.Logger,
            locale: Locale,
        ): PublicApi.CustomInterpreter = RepoNameInterpreter()
    }
}

private data class CapitalizeNode(
    val word: String,
) : PublicApi.Node

private class CapitalizeParser(
    logger: PublicApi.Logger,
    controller: PublicApi.ParserPluginController,
) : PublicApi.ParserPlugin, SharedParserRules(logger, controller) {
    override fun parse(tokenizer: PublicApi.TokenStore): PublicApi.Node {
        tokenizer.shift()
        return CapitalizeNode(tokenizer.resolveValues().first())
    }

    companion object : PublicApi.ParserPluginFactory {
        override fun createPlugin(
            logger: PublicApi.Logger,
            controller: PublicApi.ParserPluginController,
        ): PublicApi.ParserPlugin = CapitalizeParser(logger, controller)
    }
}

private class CapitalizeInterpreter : PublicApi.CustomInterpreter {
    override fun interpret(
        node: CoreNode.FunctionNode,
        controller: PublicApi.InterpreterController,
    ): String {
        val arguments = node.arguments as CoreNode.CompoundNode
        val word = (arguments.children.first() as CapitalizeNode).word

        return word.replaceFirstChar {
            if (it.isLowerCase()) {
                it.titlecase()
            } else {
                it.toString()
            }
        }
    }

    companion object : PublicApi.InterpreterFactory {
        override fun getInstance(
            logger: PublicApi.Logger,
            locale: Locale,
        ): PublicApi.CustomInterpreter = CapitalizeInterpreter()
    }
}
