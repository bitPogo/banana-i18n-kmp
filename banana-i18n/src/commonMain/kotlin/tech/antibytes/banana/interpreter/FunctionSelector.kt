/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode.FunctionNode

internal class FunctionSelector(
    private val defaultInterpreter: BananaContract.InterpreterPlugin<FunctionNode>,
    private val registerPlugins: Map<String, PublicApi.ParameterizedInterpreterPlugin<FunctionNode>>
) : PublicApi.ParameterizedInterpreterPlugin<FunctionNode> {
    override fun interpret(node: FunctionNode, controller: PublicApi.InterpreterController): String {
        return if (registerPlugins.containsKey(node.id)) {
            registerPlugins[node.id]!!.interpret(node, controller)
        } else {
            defaultInterpreter.interpret(node)
        }
    }
}
