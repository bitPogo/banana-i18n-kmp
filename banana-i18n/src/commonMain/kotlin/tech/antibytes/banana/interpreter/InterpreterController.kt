/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.InternalNodes

internal class InterpreterController(
    private val textInterpreter: BananaContract.InterpreterPlugin<InternalNodes.TextNode>,
) : BananaContract.InterpreterController {
    override fun interpret(node: BananaContract.Node): String {
        return when (node) {
            is InternalNodes.TextNode -> textInterpreter.interpret(node)
            else -> TODO()
        }
    }
}
