/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

class InterpreterControllerStub(
    var interpret: ((node: PublicApi.Node) -> String)? = null,
) : PublicApi.InterpreterController, MockContract.Mock {
    override fun interpret(node: PublicApi.Node): String {
        return interpret?.invoke(node) ?: throw MockError.MissingStub("No interpret sideeffect was given!")
    }

    override fun clear() {
        interpret = null
    }
}
