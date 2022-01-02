/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import com.appmattus.kotlinfixture.kotlinFixture
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.mock.interpreter.InterpreterControllerStub
import tech.antibytes.mock.parser.BananaParserStub
import tech.antibytes.mock.parser.TokenStoreFake
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import kotlin.test.Test

class BananaI18nSpec {
    private val fixture = kotlinFixture()

    @Test
    fun `It fulfils BananaI18n`() {
        BananaI18n(koinApplication()) fulfils PublicApi.BananaI18n::class
    }

    @Test
    fun `Given i18n is called with only a message, it delegates the message to the Tokenizer and a EmptyMap to the Interpreter`() {
        // Given
        val message: String = fixture()

        var capturedMessage: String? = null
        var capturedVariables: Map<String, String>? = null

        val koin = koinApplication {
            modules(
                module {
                    single<PublicApi.TokenStore> { parameter ->
                        capturedMessage = parameter.get()
                        TokenStoreFake()
                    }

                    single<BananaContract.TopLevelParser> {
                        BananaParserStub {
                            CoreNode.CompoundNode(emptyList())
                        }
                    }

                    single<PublicApi.InterpreterController> { parameter ->
                        capturedVariables = parameter.get()
                        InterpreterControllerStub { "" }
                    }
                }
            )
        }

        val banana = BananaI18n(koin)

        // When
        banana.i18n(message)

        // Then
        capturedMessage!! mustBe message
        capturedVariables!! mustBe emptyMap<String, String>()
    }

    @Test
    fun `Given i18n is called with only a message with a Map, it delegates the message to the Tokenizer and the given Map to the Interpreter`() {
        // Given
        val message: String = fixture()
        val variables: Map<String, String> = fixture()

        var capturedMessage: String? = null
        var capturedVariables: Map<String, String>? = null

        val koin = koinApplication {
            modules(
                module {
                    single<PublicApi.TokenStore> { parameter ->
                        capturedMessage = parameter.get()
                        TokenStoreFake()
                    }

                    single<BananaContract.TopLevelParser> {
                        BananaParserStub {
                            CoreNode.CompoundNode(emptyList())
                        }
                    }

                    single<PublicApi.InterpreterController> { parameter ->
                        capturedVariables = parameter.get()
                        InterpreterControllerStub { "" }
                    }
                }
            )
        }

        val banana = BananaI18n(koin)

        // When
        banana.i18n(message, variables)

        // Then
        capturedMessage!! mustBe message
        capturedVariables!! mustBe variables
    }

    @Test
    fun `Given i18n is called with only a message with a variable amount of String, it delegates the message to the Tokenizer and a Variables as Map by their index to the Interpreter`() {
        // Given
        val message: String = fixture()
        val variables: Array<String> = fixture()

        val expectedVariables = variables
            .mapIndexed { idx, value ->
                idx.toString() to value
            }
            .toMap()

        var capturedMessage: String? = null
        var capturedVariables: Map<String, String>? = null

        val koin = koinApplication {
            modules(
                module {
                    single<PublicApi.TokenStore> { parameter ->
                        capturedMessage = parameter.get()
                        TokenStoreFake()
                    }

                    single<BananaContract.TopLevelParser> {
                        BananaParserStub {
                            CoreNode.CompoundNode(emptyList())
                        }
                    }

                    single<PublicApi.InterpreterController> { parameter ->
                        capturedVariables = parameter.get()
                        InterpreterControllerStub { "" }
                    }
                }
            )
        }

        val banana = BananaI18n(koin)

        // When
        banana.i18n(message, *variables)

        // Then
        capturedMessage!! mustBe message
        capturedVariables!! mustBe expectedVariables
    }

    @Test
    fun `Given i18n is called with a message, it returns the interpreted Messages`() {
        // Given
        val message: String = fixture()
        val expected: String = fixture()

        val tokenStore = TokenStoreFake()
        val rootNode = CoreNode.CompoundNode(emptyList())

        var capturedTokenStore: PublicApi.TokenStore? = null
        var capturedRootNode: PublicApi.Node? = null

        val koin = koinApplication {
            modules(
                module {
                    single<PublicApi.TokenStore> {
                        tokenStore
                    }

                    single<BananaContract.TopLevelParser> {
                        BananaParserStub { givenStore ->
                            capturedTokenStore = givenStore
                            rootNode
                        }
                    }

                    single<PublicApi.InterpreterController> {
                        InterpreterControllerStub { givenRootNode ->
                            capturedRootNode = givenRootNode
                            expected
                        }
                    }
                }
            )
        }

        val banana = BananaI18n(koin)

        // When
        val actual = banana.i18n(message)

        // Then
        actual mustBe expected
        capturedTokenStore!! mustBe tokenStore
        capturedRootNode!! mustBe rootNode
    }
}
