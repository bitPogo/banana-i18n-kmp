/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.InterpreterController
import tech.antibytes.banana.ast.CoreNodes

internal class CompoundInterpreter : BananaContract.ParameterizedInterpreterPlugin<CoreNodes.CompoundNode, InterpreterController> {
    override fun interpret(node: CoreNodes.CompoundNode, parameter: InterpreterController): String {
        val output = StringBuilder(node.children.size)

        node.children.forEach { child ->
            output.append(
                parameter.interpret(child)
            )
        }

        return output.toString()
    }
}
