/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract.InterpreterController
import tech.antibytes.banana.BananaContract.InterpreterPlugin
import tech.antibytes.banana.BananaContract.ParameterizedInterpreterPlugin
import tech.antibytes.banana.ast.CoreNodes.FunctionNode

internal class FunctionSelector(
    private val defaultInterpreter: InterpreterPlugin<FunctionNode>,
    private val registerPlugins: Map<String, ParameterizedInterpreterPlugin<FunctionNode, InterpreterController>>
) : ParameterizedInterpreterPlugin<FunctionNode, InterpreterController> {
    override fun interpret(node: FunctionNode, parameter: InterpreterController): String {
        return if (registerPlugins.containsKey(node.id)) {
            registerPlugins[node.id]!!.interpret(node, parameter)
        } else {
            defaultInterpreter.interpret(node)
        }
    }
}
