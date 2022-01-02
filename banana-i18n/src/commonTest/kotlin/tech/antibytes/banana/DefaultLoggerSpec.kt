/*
 * Copyright (c) 2022 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.banana

import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.sameAs
import kotlin.test.Test

class DefaultLoggerSpec {
    @Test
    fun `It fulfils Logger`() {
        DefaultLogger() fulfils PublicApi.Logger::class
    }

    @Test
    fun `Given warn is called with a Tag and a Message, it does nothing`() {
        // Given
        val logger = DefaultLogger()

        // When
        val result = logger.warning(PublicApi.Tag.PARSER, "something")

        // Then
        result sameAs Unit
    }

    @Test
    fun `Given error is called with a Tag and a Message, it does nothing`() {
        // Given
        val logger = DefaultLogger()

        // When
        val result = logger.error(PublicApi.Tag.PARSER, "something")

        // Then
        result sameAs Unit
    }
}
