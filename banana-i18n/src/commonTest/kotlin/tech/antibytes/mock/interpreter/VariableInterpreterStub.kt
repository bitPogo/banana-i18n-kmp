/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.Variables
import tech.antibytes.banana.ast.CoreNode
import tech.antibytes.util.test.MockContract

internal class VariableInterpreterStub(
    var interpret: (CoreNode.VariableNode, Variables) -> String = { _, _ -> "" },
) : BananaContract.VariableInterpreter<CoreNode.VariableNode>, MockContract.Mock {
    override fun interpret(
        node: CoreNode.VariableNode,
        variables: Variables,
    ): String = interpret.invoke(node, variables)

    override fun clear() {
        interpret = { _, _ -> "" }
    }
}
