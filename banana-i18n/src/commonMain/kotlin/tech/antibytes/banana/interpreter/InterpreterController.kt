/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.InterpreterPlugin
import tech.antibytes.banana.BananaContract.ParameterizedInterpreterPlugin
import tech.antibytes.banana.ast.CoreNodes

internal class InterpreterController(
    private val parameter: Map<String, String>,
    private val variableInterpreter: ParameterizedInterpreterPlugin<CoreNodes.VariableNode, Map<String, String>>,
    private val textInterpreter: InterpreterPlugin<CoreNodes.TextNode>,
    private val functionSelector: ParameterizedInterpreterPlugin<CoreNodes.FunctionNode, BananaContract.InterpreterController>,
    private val compoundInterpreter: ParameterizedInterpreterPlugin<CoreNodes.CompoundNode, BananaContract.InterpreterController>
) : BananaContract.InterpreterController {
    override fun interpret(node: BananaContract.Node): String {
        return when (node) {
            is CoreNodes.TextNode -> textInterpreter.interpret(node)
            is CoreNodes.VariableNode -> variableInterpreter.interpret(node, parameter)
            is CoreNodes.FunctionNode -> functionSelector.interpret(node, this)
            is CoreNodes.CompoundNode -> compoundInterpreter.interpret(node, this)
            else -> TODO()
        }
    }
}
