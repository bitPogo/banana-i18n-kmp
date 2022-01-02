/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import org.koin.core.module.Module
import org.koin.core.qualifier.named
import org.koin.dsl.module
import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode

internal fun resolveInterpreterModule(): Module {
    return module {
        single<BananaContract.NodeConcatenator> {
            NodeConcatenator()
        }

        single<BananaContract.InterpreterPlugin<CoreNode.TextNode>>(named(BananaContract.KoinLabels.TEXT_INTERPRETER)) {
            TextInterpreter(get())
        }

        single<BananaContract.VariableInterpreter<CoreNode.VariableNode>>(named(BananaContract.KoinLabels.VARIABLE_INTERPRETER)) {
            VariableInterpreter(get())
        }

        single<BananaContract.InterpreterPlugin<CoreNode.FunctionNode>>(named(BananaContract.KoinLabels.FUNCTION_INTERPRETER)) {
            DefaultFunctionInterpreter(get())
        }

        single<PublicApi.ParameterizedInterpreterPlugin<CoreNode.FunctionNode>>(named(BananaContract.KoinLabels.FUNCTION_SELECTOR)) {
            FunctionInterpreterSelector(
                get(named(BananaContract.KoinLabels.FUNCTION_INTERPRETER)),
                get(named(BananaContract.KoinLabels.INTERPRETER_PLUGINS))
            )
        }

        single<PublicApi.ParameterizedInterpreterPlugin<CoreNode.CompoundNode>>(named(BananaContract.KoinLabels.COMPOUND_INTERPRETER)) {
            CompoundInterpreter(
                get()
            )
        }

        single<PublicApi.ParameterizedInterpreterPlugin<CoreNode.LinkNode>>(named(BananaContract.KoinLabels.LINK_INTERPRETER)) {
            LinkInterpreter(
                get(),
                get()
            )
        }

        single<PublicApi.ParameterizedInterpreterPlugin<CoreNode.FreeLinkNode>>(named(BananaContract.KoinLabels.FREE_LINK_INTERPRETER)) {
            FreeLinkInterpreter(
                get(),
                get()
            )
        }

        factory<PublicApi.InterpreterController> { parameter ->
            BananaInterpreter(
                parameter.get(),
                get(named(BananaContract.KoinLabels.VARIABLE_INTERPRETER)),
                get(named(BananaContract.KoinLabels.TEXT_INTERPRETER)),
                get(named(BananaContract.KoinLabels.FUNCTION_SELECTOR)),
                get(named(BananaContract.KoinLabels.COMPOUND_INTERPRETER)),
                get(named(BananaContract.KoinLabels.LINK_INTERPRETER)),
                get(named(BananaContract.KoinLabels.FREE_LINK_INTERPRETER)),
            )
        }
    }
}
