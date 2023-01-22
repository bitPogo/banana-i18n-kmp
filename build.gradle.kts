/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */
import tech.antibytes.gradle.banana.config.publishing.BananaPublishingConfiguration
import tech.antibytes.gradle.banana.config.repositories.Repositories.bananaRepositories
import tech.antibytes.gradle.dependency.helper.addCustomRepositories
import tech.antibytes.gradle.dependency.helper.ensureKotlinVersion
import tech.antibytes.gradle.quality.api.LinterConfiguration
import tech.antibytes.gradle.quality.api.PartialLinterConfiguration

plugins {
    id("tech.antibytes.gradle.setup")

    alias(antibytesCatalog.plugins.gradle.antibytes.dependencyHelper)
    alias(antibytesCatalog.plugins.gradle.antibytes.publishing)
    alias(antibytesCatalog.plugins.gradle.antibytes.quality)
}

val publishing = BananaPublishingConfiguration(project)

antibytesPublishing {
    versioning.set(publishing.versioning)
    repositories.set(publishing.repositories)
}

val codeConfiguration = LinterConfiguration().code as PartialLinterConfiguration

antibytesQuality {
    this.linter.set(
        LinterConfiguration(
            code = codeConfiguration.copy(
                exclude = listOf(
                    codeConfiguration.exclude,
                    setOf("banana-i18n/src/commonMain/kotlin/tech/antibytes/banana/tokenizer/Character.kt")
                ).flatten().toSet()
            )
        )
    )
}

allprojects {
    repositories {
        addCustomRepositories(bananaRepositories)
        mavenCentral()
        google()
        jcenter()
    }

    ensureKotlinVersion()
}

tasks.named<Wrapper>("wrapper") {
    gradleVersion = "7.5.1"
    distributionType = Wrapper.DistributionType.ALL
}
