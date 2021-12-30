/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.MockContract
import tech.antibytes.util.test.MockError

internal class NodeFactoryStub(
    var createNode: ((children: List<PublicApi.Node>) -> PublicApi.Node)? = null
) : PublicApi.NodeFactory, MockContract.Mock {
    override fun createNode(children: List<PublicApi.Node>): PublicApi.Node {
        return createNode?.invoke(children)
            ?: throw MockError.MissingStub("Missing sideeffect for createNode")
    }

    override fun clear() {
        createNode = null
    }
}
