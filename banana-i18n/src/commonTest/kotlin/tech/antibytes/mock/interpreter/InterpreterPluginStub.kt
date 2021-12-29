/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

class InterpreterPluginStub<T: BananaContract.Node>(
    var interpret: ((node: T) -> String)? = null
): BananaContract.InterpreterPlugin<T>, MockContract.Mock {
    override fun interpret(node: T): String {
        return interpret?.invoke(node) ?: throw MockError.MissingStub("No nested interpreter were given!")
    }

    override fun clear() {
        interpret = null
    }
}
