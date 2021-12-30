/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.InterpreterController
import tech.antibytes.banana.ast.CoreNode

internal class CompoundInterpreter(
    private val concatenator: BananaContract.NodeConcatenator
) : BananaContract.ParameterizedInterpreterPlugin<CoreNode.CompoundNode, InterpreterController> {
    override fun interpret(
        node: CoreNode.CompoundNode,
        parameter: InterpreterController
    ): String = concatenator.concatenate(node.children, parameter)
}
