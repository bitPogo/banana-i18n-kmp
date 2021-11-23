/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.dependency

object Version {

    val gradle = Gradle

    object Gradle {
        /**
         * [Kotlin](https://kotlinlang.org/docs/releases.html)
         */
        const val kotlin = "1.5.31"

        /**
         * [AGP](https://developer.android.com/studio/releases/gradle-plugin)
         */
        const val android = "7.0.3"

        /**
         * [OWASP](https://jeremylong.github.io/DependencyCheck/dependency-check-gradle/index.html)
         */
        const val owasp = "6.4.1"

        /**
         * [AnitBytes GradlePlugins](https://github.com/bitPogo/gradle-plugins)
         */
        const val antibytes = "ce85ff3"

        /**
         * [Spotless](https://plugins.gradle.org/plugin/com.diffplug.gradle.spotless)
         */
        const val spotless = "5.14.3"

        /**
         * [KTLint](https://github.com/pinterest/ktlint)
         */
        const val ktlint = "0.42.1"
    }

    val tokenizer = Tokenizer

    object Tokenizer {
        /**
         * [JFlex](https://jflex.de/)
         */
        const val jflex = "1.8.2"
    }
}
