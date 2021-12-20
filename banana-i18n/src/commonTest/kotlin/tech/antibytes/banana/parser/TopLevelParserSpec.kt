/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import com.appmattus.kotlinfixture.kotlinFixture
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.Companion.EOF
import tech.antibytes.banana.ast.CompoundNode
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.AfterTest
import kotlin.test.Test

class TopLevelParserSpec {
    private val fixture = kotlinFixture()
    private val tokenStore = TokenStoreFake()
    private val logger = LoggerStub()

    @AfterTest
    fun tearDown() {
        tokenStore.clear()
    }

    @Test
    fun `It fulfils TopLevelParser`() {
        val parser: Any = TopLevelParser(logger)

        parser fulfils BananaContract.TopLevelParser::class
    }

    @Test
    fun `Given parse is called it accepts Empty Messages`() {
        // Given
        val parser = TopLevelParser(logger)
        tokenStore.tokens.addAll(listOf(EOF, EOF))

        // When
        val message = parser.parse(tokenStore)

        // Then
        message fulfils CompoundNode::class
        (message as CompoundNode).children.isEmpty() mustBe true
    }

    // TODO Mixed on Message Level
}
