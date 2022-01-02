/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import org.koin.core.KoinApplication
import org.koin.core.parameter.parametersOf

internal class BananaI18n(
    private val di: KoinApplication
) : PublicApi.BananaI18n {
    private val parser: BananaContract.TopLevelParser by di.koin.inject()

    private fun mapVarArgs(args: Array<out String>): Map<String, String> {
        return args
            .mapIndexed { index, value -> (index + 1).toString() to value }
            .toMap()
    }

    override fun i18n(message: String, vararg parameter: String): String {
        return i18n(
            message,
            mapVarArgs(parameter)
        )
    }

    override fun i18n(message: String, parameter: Map<String, String>): String {
        val tokenizer: PublicApi.TokenStore = di.koin.get(
            parameters = {
                parametersOf(message)
            }
        )

        val interpreter: PublicApi.InterpreterController = di.koin.get(
            parameters = {
                parametersOf(parameter)
            }
        )

        return interpreter.interpret(
            parser.parse(tokenizer)
        )
    }
}
