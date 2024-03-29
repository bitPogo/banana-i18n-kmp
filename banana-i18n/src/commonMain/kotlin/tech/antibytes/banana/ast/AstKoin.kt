/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.ast

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi

internal fun resolveAstModule(): Module {
    return module {
        single<PublicApi.NodeFactory>(named(BananaContract.KoinLabels.COMPOUND_FACTORY)) {
            CoreNode.CompoundNode
        }
    }
}
