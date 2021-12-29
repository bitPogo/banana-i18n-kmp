/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.CoreNodes.TextNode

internal class TextInterpreter(
    private val textInterceptor: BananaContract.TextInterceptor
) : BananaContract.InterpreterPlugin<TextNode> {
    override fun interpret(node: TextNode): String {
        return node.chunks
            .map { chunk -> textInterceptor.intercept(chunk) }
            .joinToString("")
    }
}
