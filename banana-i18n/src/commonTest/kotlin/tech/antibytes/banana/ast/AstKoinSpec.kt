/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.ast

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class AstKoinSpec {
    @Test
    fun `Given resoloveAstKoin is called it contains a NodeFactory tagged with CompoundNode`() {
        // Given
        val koin = koinApplication {
            modules(resolveAstKoin())
        }

        // When
        val nodeFactory: PublicApi.NodeFactory = koin.koin.get(named("CompoundNode"))

        // Then
        nodeFactory fulfils PublicApi.NodeFactory::class
    }
}
