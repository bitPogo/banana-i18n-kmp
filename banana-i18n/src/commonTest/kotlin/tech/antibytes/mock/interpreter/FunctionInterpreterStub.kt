/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.InternalNodes.FunctionNode
import tech.antibytes.util.test.MockContract

internal class FunctionInterpreterStub(
    var interpret: (FunctionNode) -> String = { node -> node.id }
) : BananaContract.InterpreterPlugin<FunctionNode>, MockContract.Mock {

    override fun interpret(node: FunctionNode): String = interpret.invoke(node)

    override fun clear() {
        interpret = { node -> node.id }
    }
}
