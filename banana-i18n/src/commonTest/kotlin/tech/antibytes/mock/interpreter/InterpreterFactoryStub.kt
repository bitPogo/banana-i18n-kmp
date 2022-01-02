/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.Locale
import tech.antibytes.banana.PublicApi
import tech.antibytes.util.test.MockError

internal class InterpreterFactoryStub(
    var getInstance: ((PublicApi.Logger, Locale) -> PublicApi.CustomInterpreter)? = null
) : PublicApi.InterpreterFactory {
    override fun getInstance(
        logger: PublicApi.Logger,
        locale: Locale
    ): PublicApi.CustomInterpreter {
        return getInstance?.invoke(logger, locale) ?: throw MockError.MissingStub("Missing sideeffect getInstance!")
    }
}
