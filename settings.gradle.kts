/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

includeBuild("gradlePlugin/banana-dependency")

plugins {
    id("com.gradle.enterprise") version("3.7")
}

include(
    ":banana-i18n"
)

buildCache {
    local {
        isEnabled = false
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "banana-i18n-kmp"
