/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.ast.TextNode

internal class TextInterpreter(
    private val textInterceptor: BananaContract.TextInterceptor
) : BananaContract.InterpreterPlugin<TextNode> {
    override fun interpret(Node: TextNode): String {
        return Node.chunks
            .map { chunk -> textInterceptor.intercept(chunk) }
            .joinToString("")
    }
}
