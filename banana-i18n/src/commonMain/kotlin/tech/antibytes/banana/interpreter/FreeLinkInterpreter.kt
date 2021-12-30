/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNode

internal class FreeLinkInterpreter(
    private val concatenator: BananaContract.NodeConcatenator,
    private val formatter: BananaContract.LinkFormatter
) : BananaContract.ParameterizedInterpreterPlugin<CoreNode.FreeLinkNode, BananaContract.InterpreterController> {
    override fun interpret(
        node: CoreNode.FreeLinkNode,
        parameter: BananaContract.InterpreterController
    ): String {
        return formatter.formatFreeLink(
            url = parameter.interpret(node.url),
            displayText = concatenator.concatenate(node.display, parameter)
        )
    }
}