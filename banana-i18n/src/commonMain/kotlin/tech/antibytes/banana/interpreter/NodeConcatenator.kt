/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract

internal class NodeConcatenator : BananaContract.NodeConcatenator {
    override fun concatenate(
        nodes: List<BananaContract.Node>,
        controller: BananaContract.InterpreterController
    ): String {
        val output = StringBuilder(nodes.size)

        nodes.forEach { node ->
            output.append(
                controller.interpret(node)
            )
        }

        return output.toString()
    }
}
