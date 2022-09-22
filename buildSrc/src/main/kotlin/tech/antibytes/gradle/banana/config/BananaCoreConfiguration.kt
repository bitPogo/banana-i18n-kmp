/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.config

import tech.antibytes.gradle.publishing.api.PackageConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration

object BananaCoreConfiguration {
    const val group = "tech.antibytes.banana-i18n-kmp"

    val publishing = Publishing

    object Publishing : BananaPublishingConfiguration() {
        val packageConfiguration = PackageConfiguration(
            pom = PomConfiguration(
                name = "banana-i18n",
                description = "A implementation of MediaWikis banana for Kotlin Multiplatform.",
                year = 2022,
                url = "https://$gitHubRepositoryPath",
            ),
            developers = listOf(developer),
            license = license,
            scm = sourceControl,
        )
    }
}
