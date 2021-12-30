/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.BananaContract.InterpreterPlugin
import tech.antibytes.banana.BananaContract.ParameterizedInterpreterPlugin
import tech.antibytes.banana.ast.CoreNode

internal class InterpreterController(
    private val parameter: Map<String, String>,
    private val variableInterpreter: ParameterizedInterpreterPlugin<CoreNode.VariableNode, Map<String, String>>,
    private val textInterpreter: InterpreterPlugin<CoreNode.TextNode>,
    private val functionSelector: ParameterizedInterpreterPlugin<CoreNode.FunctionNode, BananaContract.InterpreterController>,
    private val compoundInterpreter: ParameterizedInterpreterPlugin<CoreNode.CompoundNode, BananaContract.InterpreterController>,
    private val linkInterpreter: ParameterizedInterpreterPlugin<CoreNode.LinkNode, BananaContract.InterpreterController>,
    private val freeLinkInterpreter: ParameterizedInterpreterPlugin<CoreNode.FreeLinkNode, BananaContract.InterpreterController>
) : BananaContract.InterpreterController {
    override fun interpret(node: BananaContract.Node): String {
        return when (node) {
            is CoreNode.TextNode -> textInterpreter.interpret(node)
            is CoreNode.VariableNode -> variableInterpreter.interpret(node, parameter)
            is CoreNode.FunctionNode -> functionSelector.interpret(node, this)
            is CoreNode.CompoundNode -> compoundInterpreter.interpret(node, this)
            is CoreNode.LinkNode -> linkInterpreter.interpret(node, this)
            is CoreNode.FreeLinkNode -> freeLinkInterpreter.interpret(node, this)
            else -> throw FatalInterpreterError("Unknown Node detected!")
        }
    }
}
