/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.VariableNode

internal class VariableInterpreter(
    private val logger: BananaContract.Logger
) : BananaContract.ParameterizedInterpreterPlugin<VariableNode, Map<String, String>> {
    override fun interpret(node: VariableNode, parameter: Map<String, String>): String {
        return parameter.getOrElse(node.id) {
            logger.error(BananaContract.Tag.INTERPRETER, "Error: Unknown variable (${node.id}).")
            "\$${node.id}"
        }
    }
}
