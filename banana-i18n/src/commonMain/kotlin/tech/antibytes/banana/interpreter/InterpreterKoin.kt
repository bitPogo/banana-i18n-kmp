/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNode

internal fun resolveInterpreterModule(): Module {
    return module {
        single<BananaContract.NodeConcatenator> {
            NodeConcatenator()
        }

        single<BananaContract.InterpreterPlugin<CoreNode.TextNode>>(named(BananaContract.KoinLabels.TEXT_INTERPRETER)) {
            TextInterpreter(get())
        }
    }
}
