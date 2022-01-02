/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.Variables
import tech.antibytes.banana.ast.CoreNode.VariableNode

internal class VariableInterpreter(
    private val logger: PublicApi.Logger
) : BananaContract.VariableInterpreter<VariableNode> {
    override fun interpret(node: VariableNode, variables: Variables): String {
        return variables.getOrElse(node.id) {
            logger.error(PublicApi.Tag.INTERPRETER, "Error: Unknown variable (${node.id}).")
            "\$${node.id}"
        }
    }
}
