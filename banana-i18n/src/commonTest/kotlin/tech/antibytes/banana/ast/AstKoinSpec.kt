/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.ast

import kotlin.test.Test
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.fulfils

class AstKoinSpec {
    @Test
    fun `Given resolveAstModule is called it contains a NodeFactory tagged with CompoundNode`() {
        // Given
        val koin = koinApplication {
            modules(resolveAstModule())
        }

        // When
        val nodeFactory: PublicApi.NodeFactory = koin.koin.get(named(BananaContract.KoinLabels.COMPOUND_FACTORY))

        // Then
        nodeFactory fulfils PublicApi.NodeFactory::class
    }
}
