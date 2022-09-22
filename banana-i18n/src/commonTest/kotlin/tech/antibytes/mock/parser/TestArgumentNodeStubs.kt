/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.PublicApi

internal class TestArgumentNode : PublicApi.Node
internal class TestArgumentsNode : PublicApi.Node {
    companion object : PublicApi.NodeFactory {
        lateinit var lastInstance: PublicApi.Node
        lateinit var lastChildren: List<PublicApi.Node>

        override fun createNode(children: List<PublicApi.Node>): PublicApi.Node {
            return TestArgumentsNode().also {
                lastChildren = children
                lastInstance = it
            }
        }
    }
}
