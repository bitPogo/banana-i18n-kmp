/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.test.utils

import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertTrue

inline infix fun <reified T : Any> Any.fulfils(type: KClass<T>) {
    assertTrue(this is T)
}

inline infix fun <reified T : Any> Any.mustBe(value: T) {
    assertEquals(
        actual = this,
        expected = value
    )
}
