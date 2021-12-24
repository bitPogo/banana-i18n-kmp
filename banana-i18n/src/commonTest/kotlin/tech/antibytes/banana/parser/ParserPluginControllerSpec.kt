/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.parser

import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.fulfils
import kotlin.test.Test

class ParserPluginControllerSpec {
    @Test
    fun `It fulfils ParserPluginController`() {
        ParserPluginController() fulfils BananaContract.ParserPluginController::class
    }
}
