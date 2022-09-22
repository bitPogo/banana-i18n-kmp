/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode

internal class FreeLinkInterpreter(
    private val concatenator: BananaContract.NodeConcatenator,
    private val formatter: PublicApi.LinkFormatter,
) : PublicApi.ParameterizedInterpreterPlugin<CoreNode.FreeLinkNode> {
    override fun interpret(node: CoreNode.FreeLinkNode, controller: PublicApi.InterpreterController): String {
        return formatter.formatFreeLink(
            url = controller.interpret(node.url),
            displayText = concatenator.concatenate(node.display, controller),
        )
    }
}
