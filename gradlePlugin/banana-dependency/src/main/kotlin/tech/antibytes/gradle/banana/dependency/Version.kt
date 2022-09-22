/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.dependency

object Version {

    val gradle = Gradle
    val jvm = Jvm
    val antibytes = Antibytes

    object Gradle {
        /**
         * [AnitBytes GradlePlugins](https://github.com/bitPogo/gradle-plugins)
         */
        const val antibytes = "9e2ffe9"

        /**
         * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
         */
        const val spotless = "6.11.0"
    }

    object Antibytes {
        const val test = "9401af5"
        const val kfixture = "0.3.1"
        const val kmock = "0.3.0-rc04"
    }

    object Jvm {
        /**
         * [ICU](https://unicode-org.github.io/icu/userguide/icu4j/)
         */
        const val icu = "70.1"
    }
}
