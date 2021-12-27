/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.parser

import tech.antibytes.banana.BananaContract

internal class TestArgumentNode : BananaContract.Node
internal class TestArgumentsNode : BananaContract.Node {
    companion object : BananaContract.NodeFactory {
        lateinit var lastInstance: BananaContract.Node
        lateinit var lastChildren: List<BananaContract.Node>

        override fun createNode(children: List<BananaContract.Node>): BananaContract.Node {
            return TestArgumentsNode().also {
                lastChildren = children
                lastInstance = it
            }
        }
    }
}
