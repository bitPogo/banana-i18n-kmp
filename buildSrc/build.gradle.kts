plugins {
    `kotlin-dsl`

    id("tech.antibytes.gradle.banana.dependency")
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

dependencies {
    implementation(tech.antibytes.gradle.banana.dependency.gradle.kotlin)
    implementation(tech.antibytes.gradle.banana.dependency.gradle.android)
    implementation(tech.antibytes.gradle.banana.dependency.gradle.owasp)

    // quality-spotless.gradle.kts
    implementation("com.diffplug.spotless:spotless-plugin-gradle:5.14.3")
    implementation("com.pinterest:ktlint:0.42.1")
}
