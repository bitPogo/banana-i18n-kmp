/*
 * Copyright (c) 2021 Matthias Geisler (bitPogo) / All rights reserved.
 *
 * Use of this source code is governed by LGPL v2.1
 */

package tech.antibytes.gradle.banana.config

import tech.antibytes.gradle.publishing.api.DeveloperConfiguration
import tech.antibytes.gradle.publishing.api.LicenseConfiguration
import tech.antibytes.gradle.publishing.api.PomConfiguration
import tech.antibytes.gradle.publishing.api.RegistryConfiguration
import tech.antibytes.gradle.publishing.api.SourceControlConfiguration

open class SharedPublishingConfiguration(
    name: String,
    description: String
) {
    private val username = System.getenv("PACKAGE_REGISTRY_UPLOAD_USERNAME")?.toString() ?: ""
    private val password = System.getenv("PACKAGE_REGISTRY_UPLOAD_TOKEN")?.toString() ?: ""

    private val githubOwner = "bitPogo"
    private val githubRepository = "banana-i18n-kmp"

    private val host = "github.com"
    private val path = "$githubOwner/$githubRepository"
    private val gitHubOwnerPath = "$host/$githubOwner"
    private val gitHubRepositoryPath = "$host/$path"

    val pom = PomConfiguration(
        name = name,
        description = description,
        year = 2021,
        url = "https://$gitHubRepositoryPath"
    )

    val license = LicenseConfiguration(
        name = "GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1",
        url = "https://www.gnu.org/licenses/old-licenses/lgpl-2.1.txt",
        distribution = "repo"
    )

    val developer = DeveloperConfiguration(
        id = githubOwner,
        name = githubOwner,
        url = "https://$host/$githubOwner",
        email = "solascriptura001+antibytes@gmail.com"
    )

    val sourceControl = SourceControlConfiguration(
        url = "git://$gitHubRepositoryPath.git",
        connection = "scm:git://$gitHubRepositoryPath.git",
        developerConnection = "scm:git://$gitHubRepositoryPath.git",
    )

    val registries = setOf(
        RegistryConfiguration(
            useGit = false,
            gitWorkDirectory = "",
            name = "GitHubPackageRegistry",
            url = "https://maven.pkg.github.com/$path",
            username = username,
            password = password
        ),
        RegistryConfiguration(
            useGit = true,
            name = "Development",
            gitWorkDirectory = "dev",
            url = "https://$gitHubOwnerPath/maven-dev",
            username = username,
            password = password
        ),
        RegistryConfiguration(
            useGit = true,
            name = "Snapshot",
            gitWorkDirectory = "snapshots",
            url = "https://$gitHubOwnerPath/maven-snapshots",
            username = username,
            password = password
        ),
        RegistryConfiguration(
            useGit = true,
            name = "Release",
            gitWorkDirectory = "releases",
            url = "https://$gitHubOwnerPath/maven-releases",
            username = username,
            password = password
        )
    )
}
