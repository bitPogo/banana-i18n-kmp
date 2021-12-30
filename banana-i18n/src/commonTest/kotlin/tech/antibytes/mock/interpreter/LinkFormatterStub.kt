/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock.interpreter

import tech.antibytes.banana.BananaContract
import tech.antibytes.util.test.MockContract

internal class LinkFormatterStub(
    var formatLink: (String, String) -> String = { _, _ -> "" },
    var formatFreeLink: (String, String) -> String = { _, _ -> "" }
) : BananaContract.LinkFormatter, MockContract.Mock {
    override fun formatLink(
        target: String,
        displayText: String
    ): String = formatLink.invoke(target, displayText)

    override fun formatFreeLink(
        url: String,
        displayText: String
    ): String = formatFreeLink.invoke(url, displayText)

    override fun clear() {
        formatLink = { _, _ -> "" }
        formatFreeLink = { _, _ -> "" }
    }
}
