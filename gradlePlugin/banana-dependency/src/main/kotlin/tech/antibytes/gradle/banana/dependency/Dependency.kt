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
        const val icu = "com.ibm.icu:icu4j:${Version.jvm.icu}"
    }

    object AntiBytes {
        const val test = "tech.antibytes.test-utils-kmp:test-utils:${Version.antibytes.test}"
        const val annotation = "tech.antibytes.test-utils-kmp:test-utils-annotations:${Version.antibytes.test}"
        const val fixture = "tech.antibytes.kfixture:core:${Version.antibytes.kfixture}"
    }
}
