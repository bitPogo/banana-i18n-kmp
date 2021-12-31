/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.dependency

object Version {

    val gradle = Gradle
    val jvm = Jvm

    object Gradle {
        /**
         * [AnitBytes GradlePlugins](https://github.com/bitPogo/gradle-plugins)
         */
        const val antibytes = "ca0dd59"

        /**
         * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
         */
        const val spotless = "6.0.4"
    }

    object Jvm {
        /**
         * [ICU](https://unicode-org.github.io/icu/userguide/icu4j/)
         */
        const val icu = "70.1"
    }
}
