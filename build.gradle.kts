/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

import tech.antibytes.gradle.banana.config.BananaPublishingConfiguration
import tech.antibytes.gradle.banana.dependency.addCustomRepositories

plugins {
    id("tech.antibytes.gradle.banana.dependency")

    id("tech.antibytes.gradle.dependency")

    id("tech.antibytes.gradle.banana.script.quality-spotless")

    id("tech.antibytes.gradle.publishing")
}

antiBytesPublishing {
    versioning = BananaPublishingConfiguration.versioning
    repositoryConfiguration = BananaPublishingConfiguration.repositories
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
