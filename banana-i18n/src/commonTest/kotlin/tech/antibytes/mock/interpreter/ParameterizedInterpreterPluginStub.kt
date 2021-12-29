/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

class ParameterizedInterpreterPluginStub<T : BananaContract.Node, P : Any>(
    var interpret: ((node: T, parameter: P) -> String)? = null
) : BananaContract.ParameterizedInterpreterPlugin<T, P>, MockContract.Mock {
    override fun interpret(node: T, parameter: P): String {
        return interpret?.invoke(node, parameter) ?: throw MockError.MissingStub("No nested interpreter were given!")
    }

    override fun clear() {
        interpret = null
    }
}
