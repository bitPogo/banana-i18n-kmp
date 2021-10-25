plugins {
    id("tech.antibytes.gradle.banana.dependency")

    id("tech.antibytes.gradle.banana.script.quality-spotless")

    id("org.owasp.dependencycheck")
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.2"
    distributionType = Wrapper.DistributionType.ALL
}
