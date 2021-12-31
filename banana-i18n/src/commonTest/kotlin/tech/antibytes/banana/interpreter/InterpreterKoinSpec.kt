/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode
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

    // TODO
    /*@Test
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
        val interpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.VariableNode, Any> = koin.koin.get(
            qualifier = named(BananaContract.KoinLabels.VARIABLE_INTERPRETER),
            parameters = { parametersOf(emptyMap<String, String>()) }
        )

        // Then
        interpreter fulfils BananaContract.InterpreterPlugin::class
    }*/
}
