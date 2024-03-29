/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.PublicApi.InterpreterController
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.util.test.MockContract

internal class CustomFunctionInterpreterStub(
    var interpret: (FunctionNode, InterpreterController) -> String = { node, _ -> node.id },
) : PublicApi.CustomInterpreter, MockContract.Mock {

    override fun interpret(node: FunctionNode, controller: InterpreterController): String {
        return interpret.invoke(node, controller)
    }

    override fun clear() {
        interpret = { node, _ -> node.id }
    }
}
