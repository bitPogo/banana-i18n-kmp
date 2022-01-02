/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.PublicApi.InterpreterController
import tech.antibytes.banana.ast.CoreNode

internal class CompoundInterpreter(
    private val concatenator: BananaContract.NodeConcatenator,
) : PublicApi.ParameterizedInterpreterPlugin<CoreNode.CompoundNode> {
    override fun interpret(
        node: CoreNode.CompoundNode,
        controller: InterpreterController
    ): String = concatenator.concatenate(node.children, controller)
}
