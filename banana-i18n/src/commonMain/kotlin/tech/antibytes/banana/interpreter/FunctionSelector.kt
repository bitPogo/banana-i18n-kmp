/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.InternalNodes.FunctionNode

internal class FunctionSelector(
    defaultInterpreter: BananaContract.InterpreterPlugin<FunctionNode>,
    registerPlugins: Map<String, BananaContract.InterpreterPlugin<FunctionNode>>
) : BananaContract.InterpreterPlugin<FunctionNode> {
    private val functionInterpreters = registerPlugins.withDefault { defaultInterpreter }

    override fun interpret(node: FunctionNode): String {
        return functionInterpreters
            .getValue(node.id)
            .interpret(node)
    }
}
