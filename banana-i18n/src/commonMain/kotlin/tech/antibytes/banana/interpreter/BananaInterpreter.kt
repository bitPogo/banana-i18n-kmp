/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.Variables
import tech.antibytes.banana.ast.CoreNode

internal class BananaInterpreter(
    private val variables: Variables,
    private val variableInterpreter: BananaContract.VariableInterpreter<CoreNode.VariableNode>,
    private val textInterpreter: BananaContract.InterpreterPlugin<CoreNode.TextNode>,
    private val functionSelector: PublicApi.ParameterizedInterpreterPlugin<CoreNode.FunctionNode>,
    private val compoundInterpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.CompoundNode>,
    private val linkInterpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.LinkNode>,
    private val freeLinkInterpreter: PublicApi.ParameterizedInterpreterPlugin<CoreNode.FreeLinkNode>
) : PublicApi.InterpreterController {
    override fun interpret(node: PublicApi.Node): String {
        return when (node) {
            is CoreNode.TextNode -> textInterpreter.interpret(node)
            is CoreNode.VariableNode -> variableInterpreter.interpret(node, variables)
            is CoreNode.FunctionNode -> functionSelector.interpret(node, this)
            is CoreNode.CompoundNode -> compoundInterpreter.interpret(node, this)
            is CoreNode.LinkNode -> linkInterpreter.interpret(node, this)
            is CoreNode.FreeLinkNode -> freeLinkInterpreter.interpret(node, this)
            else -> throw FatalInterpreterError("Unknown Node detected!")
        }
    }
}
