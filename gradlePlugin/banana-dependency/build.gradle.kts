plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

// To make it available as direct dependency
group = "tech.antibytes.gradle.banana.dependency"
version = "1.0.0-SNAPSHOT"

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

gradlePlugin {
    plugins.register("tech.antibytes.gradle.banana.dependency") {
        id = "tech.antibytes.gradle.banana.dependency"
        implementationClass = "tech.antibytes.gradle.banana.dependency.DependencyPlugin"
    }
}
