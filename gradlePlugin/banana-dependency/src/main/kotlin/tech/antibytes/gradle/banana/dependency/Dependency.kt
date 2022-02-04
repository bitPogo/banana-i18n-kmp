/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.dependency

object Dependency {
    val gradle = GradlePlugin
    val jvm = Jvm
    val antibytes = AntiBytes

    object Jvm {
        val icu = "com.ibm.icu:icu4j:${Version.jvm.icu}"
    }

    object AntiBytes {
        val test = "tech.antibytes.test-utils-kmp:test-utils:${Version.antibytes.test}"
        val annotation = "tech.antibytes.test-utils-kmp:test-utils-annotation:${Version.antibytes.test}"
        val fixture = "tech.antibytes.test-utils-kmp:test-utils-fixture:${Version.antibytes.test}"
    }
}
