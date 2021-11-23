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

include("banana-i18n")

buildCache {
    local {
        isEnabled = true
        directory = File(rootDir, "build-cache")
        removeUnusedEntriesAfterDays = 30
    }
}

rootProject.name = "banana-i18n-kmp"
