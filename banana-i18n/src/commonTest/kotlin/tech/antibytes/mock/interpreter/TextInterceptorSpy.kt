/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.MockContract

internal class TextInterceptorSpy(
    var intercept: (String) -> String = { chunk -> chunk },
) : BananaContract.TextInterceptor, MockContract.Mock {

    override fun intercept(chunk: String): String {
        return intercept.invoke(chunk)
    }

    override fun clear() {
        intercept = { chunk -> chunk }
    }
}
