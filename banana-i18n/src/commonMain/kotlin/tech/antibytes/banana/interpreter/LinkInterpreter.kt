/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNodes

internal class LinkInterpreter(
    private val concatenator: BananaContract.NodeConcatenator,
    private val formatter: BananaContract.LinkFormatter
) : BananaContract.ParameterizedInterpreterPlugin<CoreNodes.LinkNode, BananaContract.InterpreterController> {
    override fun interpret(
        node: CoreNodes.LinkNode,
        parameter: BananaContract.InterpreterController
    ): String {
        return formatter.formatLink(
            target = concatenator.concatenate(node.target, parameter),
            displayText = concatenator.concatenate(node.display, parameter)
        )
    }
}
