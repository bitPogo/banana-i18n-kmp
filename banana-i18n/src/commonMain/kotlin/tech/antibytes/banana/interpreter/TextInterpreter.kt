/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.banana.PublicApi
import tech.antibytes.banana.ast.CoreNode.TextNode

internal class TextInterpreter(
    private val textInterceptor: PublicApi.TextInterceptor,
) : BananaContract.InterpreterPlugin<TextNode> {
    override fun interpret(node: TextNode): String {
        return node.chunks
            .map { chunk -> textInterceptor.intercept(chunk) }
            .joinToString("")
    }
}
