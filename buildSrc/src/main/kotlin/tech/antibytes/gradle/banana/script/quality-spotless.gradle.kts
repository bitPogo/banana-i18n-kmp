package tech.antibytes.gradle.banana.script

import tech.antibytes.gradle.dependency.Version

/**
 * Quality check to keep the code spotless using [Spotless](https://github.com/diffplug/spotless)
 *
 * It uses Ktlint to format and validate Kotlin code style.
 *
 * Install:
 *
 * You need to add following dependencies to the buildSrc/build.gradle.kts
 *
 * dependencies {
 *     implementation("com.diffplug.spotless:spotless-plugin-gradle:5.14.3"")
 *     implementation("com.pinterest:ktlint:0.42.3") or higher
 * }
 *
 * and ensure that the gradlePluginPortal is available
 *
 * repositories {
 *     gradlePluginPortal()
 * }
 *
 * Now just add id("tech.antibytes.gradle.banana.script.quality-spotless") to your rootProject build.gradle.kts plugins
 *
 * plugins {
 *     id("tech.antibytes.gradle.banana.script.quality-spotless")
 * }
 */
plugins {
    id("com.diffplug.spotless")
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("buildSrc/build/", "**/buildSrc/build/")
        ktlint(Version.gradle.ktLint).userData(
            mapOf(
                "disabled_rules" to "no-wildcard-imports",
                "ij_kotlin_imports_layout" to "*"
            )
        )
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(Version.gradle.ktLint)
        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
    format("misc") {
        target("**/*.adoc", "**/*.md", "**/.gitignore", ".java-version")

        trimTrailingWhitespace()
        indentWithSpaces()
        endWithNewline()
    }
}
