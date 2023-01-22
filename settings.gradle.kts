/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */
import tech.antibytes.gradle.dependency.settings.localGithub

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        val antibytesPlugins = "^tech\\.antibytes\\.[\\.a-z\\-]+"
        gradlePluginPortal()
        google()
        mavenCentral()
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-snapshots/main/snapshots")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
        maven {
            setUrl("https://raw.github.com/bitPogo/maven-rolling-releases/main/rolling")
            content {
                includeGroupByRegex(antibytesPlugins)
            }
        }
    }
}

plugins {
    id("tech.antibytes.gradle.dependency.settings") version "022f831"
}

includeBuild("setup")

include(
    ":banana-i18n"
)

dependencyResolutionManagement {
    versionCatalogs {
        getByName("antibytesCatalog") {
            version("koin-core", "3.1.6")
        }
    }
}

buildCache {
    localGithub()
}

// see: https://github.com/gradle/gradle/issues/16608
rootProject.name = "banana-i18n-kmp"
