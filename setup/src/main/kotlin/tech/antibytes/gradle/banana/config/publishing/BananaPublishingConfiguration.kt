/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.config.publishing

import org.gradle.api.Project
import tech.antibytes.gradle.configuration.BasePublishingConfiguration

open class BananaPublishingConfiguration(
    project: Project,
) : BasePublishingConfiguration(project, "banana-i18n-kmp") {
    val description = "Banana-I18n-KMP - A Kotlin Multiplatform spin-off of Banana Internationalization."
    val url = "https://$gitHubRepositoryPath"
    val year = 2022
}
