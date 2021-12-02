/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.util.test.annotations

actual object PlatformRunner {
    actual fun androidOnly(): String {
        throw RuntimeException()
    }

    actual fun jvmOnly(): String {
        return "test"
    }
}