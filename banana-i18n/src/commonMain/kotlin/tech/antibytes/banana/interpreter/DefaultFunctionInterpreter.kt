/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode.FunctionNode

internal class DefaultFunctionInterpreter(
    private val logger: PublicApi.Logger
) : BananaContract.InterpreterPlugin<FunctionNode> {
    override fun interpret(node: FunctionNode): String {
        logger.error(
            PublicApi.Tag.INTERPRETER,
            "Error: Unknown function ${node.id} in use."
        )

        return "{{${node.id}}}"
    }
}
