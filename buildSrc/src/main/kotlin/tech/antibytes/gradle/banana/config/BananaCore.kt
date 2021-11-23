/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.config

import tech.antibytes.gradle.publishing.api.VersioningConfiguration

object BananaCore {
    const val group = "tech.antibytes"

    val publishing = Publishing

    object Publishing : SharedPublishingConfiguration(
        name = "banana-i18n",
        description = "A implementation of MediaWikis banana for Kotlin Multiplatform."
    ) {
        val versioning = VersioningConfiguration(
            featurePattern = "core/(.*)".toRegex()
        )
    }

    val android = BananaCore

    object BananaCore {
        const val minSdkVersion = 24
        const val compileSdkVersion = 30
        const val targetSdkVersion = 30

        const val resourcePrefix = "banana_i18n_"
    }
}
