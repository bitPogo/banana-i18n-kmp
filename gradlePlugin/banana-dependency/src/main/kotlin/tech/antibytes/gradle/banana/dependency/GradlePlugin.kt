/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.dependency

object GradlePlugin {
    const val android = "com.android.tools.build:gradle:${Version.gradle.android}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Version.gradle.kotlin}"
    const val owasp = "org.owasp:dependency-check-gradle:${Version.gradle.owasp}"
    const val dependency = "tech.antibytes.gradle-plugins:antibytes-dependency:${Version.gradle.antibytes}"
    const val publishing = "tech.antibytes.gradle-plugins:antibytes-publishing:${Version.gradle.antibytes}"
    const val coverage = "tech.antibytes.gradle-plugins:antibytes-coverage:${Version.gradle.antibytes}"
    const val spotless = "com.diffplug.spotless:spotless-plugin-gradle:${Version.gradle.spotless}"
    const val ktlint = "com.pinterest:ktlint:${Version.gradle.ktlint}"
}
