/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.dependency

object Dependency {
    val gradle = GradlePlugin

    object Tokenizer {
        val jflex = "de.jflex:${Version.tokenizer.jflex}"
    }
}