/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

class ParameterizedInterpreterPluginStub<T : PublicApi.Node>(
    var interpret: ((node: T, parameter: PublicApi.InterpreterController) -> String)? = null,
) : PublicApi.ParameterizedInterpreterPlugin<T>, MockContract.Mock {
    override fun interpret(node: T, controller: PublicApi.InterpreterController): String {
        return interpret?.invoke(node, controller) ?: throw MockError.MissingStub("No nested interpreter were given!")
    }

    override fun clear() {
        interpret = null
    }
}
