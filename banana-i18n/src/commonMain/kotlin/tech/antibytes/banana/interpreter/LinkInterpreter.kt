/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode

internal class LinkInterpreter(
    private val concatenator: BananaContract.NodeConcatenator,
    private val formatter: PublicApi.LinkFormatter,
) : PublicApi.ParameterizedInterpreterPlugin<CoreNode.LinkNode> {
    override fun interpret(
        node: CoreNode.LinkNode,
        controller: PublicApi.InterpreterController,
    ): String {
        return formatter.formatLink(
            target = concatenator.concatenate(node.target, controller),
            displayText = concatenator.concatenate(node.display, controller),
        )
    }
}
