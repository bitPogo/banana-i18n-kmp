/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import tech.antibytes.banana.tokenizer.TokenizerContract

internal typealias Variables = Map<String, String>

internal interface BananaContract {
    interface Tokenizer {
        fun setReader(reader: TokenizerContract.Reader)
        fun next(): PublicApi.Token
    }

    interface TokenizerFactory {
        fun getInstance(reader: TokenizerContract.Reader): Tokenizer
    }

    interface TokenStoreResetter {
        val tokenizer: Tokenizer

        fun reset()
    }

    interface TopLevelParser : PublicApi.ParserPlugin

    interface NodeConcatenator {
        fun concatenate(nodes: List<PublicApi.Node>, controller: PublicApi.InterpreterController): String
    }

    interface VariableInterpreter<T : PublicApi.Node> : PublicApi.Interpreter<T> {
        fun interpret(node: T, variables: Variables): String
    }

    interface InterpreterPlugin<T : PublicApi.Node> {
        fun interpret(node: T): String
    }

    enum class KoinLabels {
        COMPOUND_FACTORY,
        DEFAULT_ARGUMENT_PARSER,
        PARSER_PLUGINS,
        TEXT_INTERPRETER,
        VARIABLE_INTERPRETER
    }

    companion object {
        val EOF = PublicApi.Token(PublicApi.TokenTypes.EOF, "", -1, -1)
    }
}
