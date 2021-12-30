/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.InterpreterController
import tech.antibytes.banana.ast.CoreNode.FunctionNode
import tech.antibytes.util.test.MockContract

internal class CustomFunctionInterpreterStub(
    var interpret: (FunctionNode, InterpreterController) -> String = { node, _ -> node.id }
) : BananaContract.ParameterizedInterpreterPlugin<FunctionNode, InterpreterController>, MockContract.Mock {

    override fun interpret(node: FunctionNode, parameter: InterpreterController): String {
        return interpret.invoke(node, parameter)
    }

    override fun clear() {
        interpret = { node, _ -> node.id }
    }
}
