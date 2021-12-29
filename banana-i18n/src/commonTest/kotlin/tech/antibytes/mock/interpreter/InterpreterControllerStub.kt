/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

class InterpreterControllerStub(
    var interpret: ((node: BananaContract.Node) -> String)? = null
) : BananaContract.InterpreterController, MockContract.Mock {
    override fun interpret(node: BananaContract.Node): String {
        return interpret?.invoke(node) ?: throw MockError.MissingStub("No interpret sideeffect was given!")
    }

    override fun clear() {
        interpret = null
    }
}
