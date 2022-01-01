/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.RegisteredInterpreterPlugins
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.mock.interpreter.FunctionInterpreterStub
import tech.antibytes.mock.interpreter.LinkFormatterStub
import tech.antibytes.mock.interpreter.TextInterceptorSpy
import tech.antibytes.mock.parser.LoggerStub
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class InterpreterKoinSpec {
    @Test
    fun `Given resolveInterpreterModule is called it contains a NodeConcatenator`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveInterpreterModule()
            )
        }

        // When
        val concatenator: BananaContract.NodeConcatenator = koin.koin.get()

        // Then
        concatenator fulfils BananaContract.NodeConcatenator::class
    }

    @Test
    fun `Given resolveInterpreterModule is called it contains a TextInterpreter`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveInterpreterModule(),
                module {
                    single<PublicApi.TextInterceptor> {
                        TextInterceptorSpy()
                    }
                }
            )
        }

        // When
        val interpreter: BananaContract.InterpreterPlugin<CoreNode.TextNode> = koin.koin.get(
            named(BananaContract.KoinLabels.TEXT_INTERPRETER)
        )

        // Then
        interpreter fulfils BananaContract.InterpreterPlugin::class
    }

    @Test
    fun `Given resolveInterpreterModule is called it contains a VariableInterpreter`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveInterpreterModule(),
                module {
                    single<PublicApi.Logger> {
                        LoggerStub()
                    }
                }
            )
        }

        // When
        val interpreter: BananaContract.VariableInterpreter<CoreNode.VariableNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.VARIABLE_INTERPRETER)
        )

        // Then
        interpreter fulfils BananaContract.VariableInterpreter::class
    }

    @Test
    fun `Given resolveInterpreterModule is called it contains a FunctionInterpreter`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveInterpreterModule(),
                module {
                    single<PublicApi.Logger> {
                        LoggerStub()
                    }
                }
            )
        }

        // When
        val interpreter: BananaContract.InterpreterPlugin<CoreNode.FunctionNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.FUNCTION_INTERPRETER)
        )

        // Then
        interpreter fulfils BananaContract.InterpreterPlugin::class
    }

    @Test
    fun `Given resolveInterpreterModule is called it contains a FunctionInterpreterSelector`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveInterpreterModule(),
                module {
                    single<BananaContract.InterpreterPlugin<CoreNode.FunctionNode>>(named(BananaContract.KoinLabels.FUNCTION_INTERPRETER)) {
                        FunctionInterpreterStub()
                    }

                    single<RegisteredInterpreterPlugins>(named(BananaContract.KoinLabels.INTERPRETER_PLUGINS)) {
                        emptyMap()
                    }
                }
            )
        }

        // When
        val interpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.FunctionNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.FUNCTION_SELECTOR)
        )

        // Then
        interpreter fulfils PublicApi.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given resolveInterpreterModule is called it contains a CompoundInterpreter`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveInterpreterModule()
            )
        }

        // When
        val interpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.CompoundNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.COMPOUND_INTERPRETER)
        )

        // Then
        interpreter fulfils PublicApi.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given resolveInterpreterModule is called it contains a LinkInterpreter`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveInterpreterModule(),
                module {
                    single<PublicApi.LinkFormatter> {
                        LinkFormatterStub()
                    }
                }
            )
        }

        // When
        val interpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.LinkNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.LINK_INTERPRETER)
        )

        // Then
        interpreter fulfils PublicApi.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given resolveInterpreterModule is called it contains a FreeLinkInterpreter`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveInterpreterModule(),
                module {
                    single<PublicApi.LinkFormatter> {
                        LinkFormatterStub()
                    }
                }
            )
        }

        // When
        val interpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.FreeLinkNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.FREE_LINK_INTERPRETER)
        )

        // Then
        interpreter fulfils PublicApi.ParameterizedInterpreterPlugin::class
    }
}
