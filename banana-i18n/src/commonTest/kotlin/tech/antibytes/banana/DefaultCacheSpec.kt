/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

internal class DefaultCacheSpec {
    @Test
    fun `It fulfils Cache`() {
        DefaultCache<Any>() fulfils PublicApi.Cache::class
    }

    @Test
    fun `Given getValue is called with a Key it returns always null`() {
        // Given
        val cache = DefaultCache<Any>()

        // When
        val result = cache.getValue("any")

        // Then
        (result == null) mustBe true
    }

    @Test
    fun `Given store is called with a Key and a Value, it does nothing`() {
        // Given
        val cache = DefaultCache<Any>()

        // When
        val result = cache.store("any", "something")

        // Then
        result sameAs Unit
    }
}
