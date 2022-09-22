/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi.InterpreterController
import tech.antibytes.banana.PublicApi.Node
import tech.antibytes.util.test.MockContract

internal class NodeConcatenatorStub(
    var concatenate: (List<Node>, InterpreterController) -> String = { _, _ -> "" },
) : BananaContract.NodeConcatenator, MockContract.Mock {
    override fun concatenate(
        nodes: List<Node>,
        controller: InterpreterController,
    ): String = concatenate.invoke(nodes, controller)

    override fun clear() {
        concatenate = { _, _ -> "" }
    }
}
