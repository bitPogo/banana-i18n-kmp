/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import kotlin.test.Test
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.RegisteredInterpreterPlugins
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.mock.LoggerStub
import tech.antibytes.mock.interpreter.FunctionInterpreterStub
import tech.antibytes.mock.interpreter.InterpreterPluginStub
import tech.antibytes.mock.interpreter.LinkFormatterStub
import tech.antibytes.mock.interpreter.ParameterizedInterpreterPluginStub
import tech.antibytes.mock.interpreter.TextInterceptorSpy
import tech.antibytes.mock.interpreter.VariableInterpreterStub
import tech.antibytes.util.test.fulfils

class InterpreterKoinSpec {
    @Test
    fun `Given resolveInterpreterModule is called it contains a NodeConcatenator`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveInterpreterModule(),
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
                },
            )
        }

        // When
        val interpreter: BananaContract.InterpreterPlugin<CoreNode.TextNode> = koin.koin.get(
            named(BananaContract.KoinLabels.TEXT_INTERPRETER),
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
                },
            )
        }

        // When
        val interpreter: BananaContract.VariableInterpreter<CoreNode.VariableNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.VARIABLE_INTERPRETER),
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
                },
            )
        }

        // When
        val interpreter: BananaContract.InterpreterPlugin<CoreNode.FunctionNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.FUNCTION_INTERPRETER),
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
                },
            )
        }

        // When
        val interpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.FunctionNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.FUNCTION_SELECTOR),
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
                resolveInterpreterModule(),
            )
        }

        // When
        val interpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.CompoundNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.COMPOUND_INTERPRETER),
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
                },
            )
        }

        // When
        val interpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.LinkNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.LINK_INTERPRETER),
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
                },
            )
        }

        // When
        val interpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.FreeLinkNode> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.FREE_LINK_INTERPRETER),
        )

        // Then
        interpreter fulfils PublicApi.ParameterizedInterpreterPlugin::class
    }

    @Test
    fun `Given resolveInterpreterModule is called it contains a InterpreterController, if variables are delegated`() {
        // Given
        val koin = koinApplication {
            allowOverride(true)
            modules(
                resolveInterpreterModule(),
                module {
                    single<BananaContract.InterpreterPlugin<CoreNode.TextNode>>(named(BananaContract.KoinLabels.TEXT_INTERPRETER)) {
                        InterpreterPluginStub()
                    }

                    single<BananaContract.VariableInterpreter<CoreNode.VariableNode>>(named(BananaContract.KoinLabels.VARIABLE_INTERPRETER)) {
                        VariableInterpreterStub()
                    }

                    single<BananaContract.InterpreterPlugin<CoreNode.FunctionNode>>(named(BananaContract.KoinLabels.FUNCTION_INTERPRETER)) {
                        InterpreterPluginStub()
                    }

                    single<PublicApi.ParameterizedInterpreterPlugin<CoreNode.FunctionNode>>(named(BananaContract.KoinLabels.FUNCTION_SELECTOR)) {
                        ParameterizedInterpreterPluginStub()
                    }

                    single<PublicApi.ParameterizedInterpreterPlugin<CoreNode.CompoundNode>>(named(BananaContract.KoinLabels.COMPOUND_INTERPRETER)) {
                        ParameterizedInterpreterPluginStub()
                    }

                    single<PublicApi.ParameterizedInterpreterPlugin<CoreNode.LinkNode>>(named(BananaContract.KoinLabels.LINK_INTERPRETER)) {
                        ParameterizedInterpreterPluginStub()
                    }

                    single<PublicApi.ParameterizedInterpreterPlugin<CoreNode.FreeLinkNode>>(named(BananaContract.KoinLabels.FREE_LINK_INTERPRETER)) {
                        ParameterizedInterpreterPluginStub()
                    }
                },
            )
        }

        // When
        val interpreter: PublicApi.InterpreterController = koin.koin.get(
            parameters = {
                parametersOf(
                    mapOf("1" to "1"),
                )
            },
        )

        // Then
        interpreter fulfils PublicApi.InterpreterController::class
    }
}
