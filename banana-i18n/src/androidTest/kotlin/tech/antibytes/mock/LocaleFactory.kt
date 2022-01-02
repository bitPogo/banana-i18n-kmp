/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.mock

import android.icu.util.ULocale
import tech.antibytes.banana.Locale

actual fun createLocale(bcp47: String) : Locale = ULocale.forLanguageTag(bcp47)
