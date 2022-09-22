/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.RegisteredInterpreterPlugins
import tech.antibytes.banana.ast.CoreNode.FunctionNode

internal class FunctionInterpreterSelector(
    private val defaultInterpreter: BananaContract.InterpreterPlugin<FunctionNode>,
    private val registeredPlugins: RegisteredInterpreterPlugins,
) : PublicApi.ParameterizedInterpreterPlugin<FunctionNode> {
    override fun interpret(node: FunctionNode, controller: PublicApi.InterpreterController): String {
        return if (registeredPlugins.containsKey(node.id)) {
            registeredPlugins[node.id]!!.interpret(node, controller)
        } else {
            defaultInterpreter.interpret(node)
        }
    }
}
