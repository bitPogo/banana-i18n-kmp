/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */
import tech.antibytes.gradle.banana.config.publishing.BananaPublishingConfiguration
import tech.antibytes.gradle.banana.dependency.addCustomRepositories

plugins {
    id("tech.antibytes.gradle.setup")

    id("tech.antibytes.gradle.banana.dependency")

    id("tech.antibytes.gradle.dependency")

    alias(antibytesCatalog.plugins.gradle.antibytes.dependencyHelper)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)
    alias(antibytesCatalog.plugins.gradle.antibytes.quality)
}

val publishing = BananaPublishingConfiguration(project)

antibytesPublishing {
    versioning.set(publishing.versioning)
    repositories.set(publishing.repositories)
}

allprojects {
    repositories {
        addCustomRepositories()
        mavenCentral()
        google()
    }
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.4"
    distributionType = Wrapper.DistributionType.ALL
}
