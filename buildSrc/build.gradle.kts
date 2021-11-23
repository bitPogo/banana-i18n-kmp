import tech.antibytes.gradle.banana.dependency.Dependency
import tech.antibytes.gradle.banana.dependency.addCustomRepositories

plugins {
    `kotlin-dsl`

    id("tech.antibytes.gradle.banana.dependency")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
    addCustomRepositories()
}

dependencies {
    implementation(Dependency.gradle.kotlin)
    implementation(Dependency.gradle.android)
    implementation(Dependency.gradle.owasp)
    implementation(Dependency.gradle.dependency)
    implementation(Dependency.gradle.publishing)
    implementation(Dependency.gradle.coverage)
    implementation(Dependency.gradle.ktlint)
    implementation(Dependency.gradle.spotless)
}
