/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana.interpreter

import tech.antibytes.banana.PublicApi

internal class DefaultLinkFormatter : PublicApi.LinkFormatter {
    private fun format(urlOrMagicWord: String, displayText: String): String {
        return if (displayText.isEmpty()) {
            urlOrMagicWord
        } else {
            displayText
        }
    }

    override fun formatLink(
        target: String,
        displayText: String
    ): String = format(target, displayText)

    override fun formatFreeLink(
        url: String,
        displayText: String
    ): String = format(url, displayText)
}
